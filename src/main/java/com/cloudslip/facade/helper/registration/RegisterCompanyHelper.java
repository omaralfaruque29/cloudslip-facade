package com.cloudslip.facade.helper.registration;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.InitialSettingStatus;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.enums.UserType;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.helper.user.AddUserHelper;
import com.cloudslip.facade.model.*;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterCompanyHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(AddUserHelper.class);

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApiAccessTokenRepository apiAccessTokenRepository;
    
    @Autowired
    private ApplicationProperties applicationProperties;

    private SaveCompanyDTO input;
    private ResponseDTO<Company> output = new ResponseDTO<Company>();
    private User existingUser;

    SystemAction systemAction = null;

    public void init(BaseInput input, Object... extraParams) {
        this.input = (SaveCompanyDTO) input;
        this.setOutput(output);
    }


    protected void checkPermission() {
        if(requester == null || requester.hasAuthority(Authority.ROLE_SUPER_ADMIN)) {
            //continue
        } else {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void checkValidity() {
        existingUser = userRepository.findByUsername(input.getAdminEmail().toLowerCase());
        if (existingUser != null && existingUser.isValid()) {
            output.generateErrorResponse("A user already exists with this username");
            throw new ApiErrorException(this.getClass().getName());
        }
        if(existingUser != null && existingUser.hasAuthority(Authority.ROLE_AGENT_SERVICE) && requester != null && !requester.hasAuthority(Authority.ROLE_SUPER_ADMIN)) {
            output.generateErrorResponse("Only Super Admin can create a user with Agent Service Role");
            throw new ApiErrorException(this.getClass().getName());
        }
        if(input.getPassword().length() < 8){
            output.generateErrorResponse("Password length error !! At least give eight character");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void doPerform() {
        systemAction = systemActionService.create(String.format("REST request to create Company: %s", input.getName()));
        if (input.getAdminEmail() == null || input.getAdminEmail().equals("")) {
            String errorMessage = "Admin Email is Required";
            systemActionService.saveWithFailure(systemAction, errorMessage);
            output.generateErrorResponse(errorMessage);
        }
        Optional<User> existingAdminUser = userRepository.findByUsernameIgnoreCase(input.getAdminEmail());
        if(existingAdminUser.isPresent()) {
            String errorMessage = String.format("A user already exists with email: %s", input.getAdminEmail());
            systemActionService.saveWithFailure(systemAction, errorMessage);
            output.generateErrorResponse(errorMessage);
        }

        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<SaveCompanyDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/company", request, ResponseDTO.class);

        if(response.getStatus() == ResponseStatus.success && response.getData() != null){
            Company company = objectMapper.convertValue(response.getData(), Company.class);

            User user = this.createNewAdminUser();
            SaveUserInfoDTO saveUserInfoDTO = createSaveUserInfoDTO(user, company);
            HttpEntity<SaveUserInfoDTO> saveUserInfoRequest = new HttpEntity<>(saveUserInfoDTO, headers);
            UserInfo userInfo = null;

            User gitUser = this.createNewGitUser();
            SaveUserInfoDTO saveGitUserInfoDTO = createSaveUserInfoDTO(gitUser, company);
            HttpEntity<SaveUserInfoDTO> saveGitUserInfoRequest = new HttpEntity<>(saveGitUserInfoDTO, headers);
            UserInfo gitUserInfo = null;
            CreateApiAccessTokenForGitUser(gitUser);

            String exceptionMessage = "";
            try {
                ResponseDTO<UserInfo> userInfoCreateResponse = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/register", saveUserInfoRequest, ResponseDTO.class);
                userInfo = objectMapper.convertValue(userInfoCreateResponse.getData(), UserInfo.class);
                ResponseDTO<UserInfo> gitUserInfoCreateResponse = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/register", saveGitUserInfoRequest, ResponseDTO.class);
                gitUserInfo = objectMapper.convertValue(gitUserInfoCreateResponse.getData(), UserInfo.class);
            } catch (ResourceAccessException ex) {
                log.info(ex.getMessage());
                exceptionMessage = ex.getMessage();
            } catch (HttpClientErrorException ex) {
                log.info(ex.getMessage());
                exceptionMessage = ex.getMessage();
            } catch (Exception ex){
                log.info(ex.getMessage());
                exceptionMessage = ex.getMessage();
            }

            user.setUserInfo(userInfo);
            userRepository.save(user);

            gitUser.setUserInfo(gitUserInfo);
            userRepository.save(gitUser);

            if(userInfo != null && gitUserInfo != null) {
                systemActionService.saveWithSuccess(systemAction);
                output.generateSuccessResponse(company, "Your Registration is Completed!");
            } else {
                systemActionService.saveWithFailure(systemAction, exceptionMessage);
                output.generateErrorResponse("Failed to register!");
            }

        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
            output.generateErrorResponse("Failed to register!");
        }
    }

    private User createNewAdminUser() {
        User user = new User();
        user.setId(ObjectId.get());
        user.setUsername(input.getAdminEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(Utils.generateRandomNumber(6));
        user.setAccountNonExpired(false);
        user.setCredentialsNonExpired(false);
        user.setEnabled(true);
        if(requester != null && requester.hasAuthority(Authority.ROLE_SUPER_ADMIN)){
            user.setNeedToResetPassword(true);
        } else {
            user.setNeedToResetPassword(false);
        }
        List<Authority> authorities = new ArrayList<>();
        authorities.add(Authority.ROLE_ADMIN);
        user.setAuthorities(authorities);
        user.setInitialSettingStatus(InitialSettingStatus.PENDING);
        user.setCreateDate(String.valueOf(LocalDateTime.now()));
        //user.setCreatedBy(requester.getUsername());
        user.setCreateActionId(systemAction.getObjectId());
        return user;
    }
    private User createNewGitUser() {
        User user = new User();
        user.setId(ObjectId.get());
        user.setUsername(input.getName().replaceAll("\\s+","").toLowerCase() + "-git-user");
        user.setPassword(passwordEncoder.encode(""));
        user.setVerificationCode(Utils.generateRandomNumber(6));
        user.setAccountNonExpired(false);
        user.setCredentialsNonExpired(false);
        user.setEnabled(true);
        user.setUserType(UserType.GIT);
        user.setNeedToResetPassword(true);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(Authority.ROLE_GIT_AGENT);
        user.setInitialSettingStatus(null);
        user.setAuthorities(authorities);
        user.setCreateDate(String.valueOf(LocalDateTime.now()));
        user.setCreateActionId(systemAction.getObjectId());
        return user;
    }

    private SaveUserInfoDTO createSaveUserInfoDTO(User user, Company company){
        SaveUserInfoDTO saveUserInfoDTO = new SaveUserInfoDTO();
        saveUserInfoDTO.setUserId(user.getObjectId());
        saveUserInfoDTO.setFirstName(company.getName());
        saveUserInfoDTO.setLastName("Admin");
        saveUserInfoDTO.setEmail(input.getAdminEmail());
        saveUserInfoDTO.setCompanyId(new ObjectId(company.getId()));
        return saveUserInfoDTO;
    }

    private void CreateApiAccessTokenForGitUser(User gitUser) {
        ApiAccessToken apiAccessToken = new ApiAccessToken();
        apiAccessToken.setUser(gitUser);
        apiAccessToken.setAccessToken(generateApiAccessToken(gitUser));
        apiAccessToken.setAllowedOrigins("*");
        apiAccessToken.setCreateDate(String.valueOf(LocalDateTime.now()));
        apiAccessToken.setCreateActionId(systemAction.getObjectId());
        apiAccessTokenRepository.save(apiAccessToken);
    }

    private String generateApiAccessToken(User user) {
        return Utils.encodeSha256(user.getId()) + Utils.generateRandomString(10);
    }

    protected void postPerformCheck() {
    }

    protected void doRollback() {
    }
}
