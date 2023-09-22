package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.SaveUserDTO;
import com.cloudslip.facade.dto.SaveUserInfoDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.Team;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class AddUserHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(AddUserHelper.class);

    private SaveUserDTO input;
    private ResponseDTO<User> output = new ResponseDTO<User>();

    private User existingUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationProperties applicationProperties;


    public void init(BaseInput input, Object... extraParams) {
        this.input = (SaveUserDTO) input;
        this.setOutput(output);
    }


    protected void checkPermission() {
        if (requester == null || (!requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && !requester.hasAuthority(Authority.ROLE_ADMIN))) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void checkValidity() {
        existingUser = userRepository.findByUsername(input.getUsername().toLowerCase());
        if (existingUser != null && existingUser.isValid()) {
            output.generateErrorResponse("A user already exists with this username");
            throw new ApiErrorException(this.getClass().getName());
        }
        if(input.hasAuthority(Authority.ROLE_AGENT_SERVICE) && !requester.hasAuthority(Authority.ROLE_SUPER_ADMIN)) {
            output.generateErrorResponse("Only Super Admin can create a user with Agent Service Role");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void doPerform() {
        User newUser = this.createNewUser();
        this.createUserInfoForUser(newUser);
    }


    private User createNewUser() {
        User newUser = new User();
        if(input.getAuthorities().size() == 0) {
            List<Authority> authorities = new ArrayList<>();
            authorities.add(Authority.ROLE_DEV);
            newUser.setAuthorities(authorities);
        } else {
            newUser.setAuthorities(input.getAuthorities());
        }
        newUser.setId(ObjectId.get());
        newUser.setUsername(input.getUsername().toLowerCase());
        if(input.hasAuthority(Authority.ROLE_AGENT_SERVICE)) {
            newUser.setPassword("");
        } else {
            newUser.setPassword(passwordEncoder.encode((input.getPassword() != null ? input.getPassword() : "123456")));
        }
        newUser.setCreateDate(String.valueOf(LocalDateTime.now()));
        newUser.setAccountNonExpired(false);
        newUser.setCredentialsNonExpired(false);
        newUser.setEnabled(true);
        newUser.setNeedToResetPassword(true);
        newUser.setVerificationCode(Utils.generateRandomNumber(6));
        newUser.setCreateActionId(actionId);
        newUser.setInitialSettingStatus(null);
        if(requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && input.getUserType() != null) {
            newUser.setUserType(input.getUserType());
        }
        return userRepository.save(newUser);
    }


    private void createUserInfoForUser(User newUser) {
        SaveUserInfoDTO saveUserInfoDTO = new SaveUserInfoDTO();
        saveUserInfoDTO.setUserId(newUser.getObjectId());
        saveUserInfoDTO.setFirstName(input.getFirstName());
        saveUserInfoDTO.setLastName(input.getLastName());
        saveUserInfoDTO.setEmail(input.getUsername());
        if(input.getCompanyId() != null){
            saveUserInfoDTO.setCompanyId(input.getCompanyId());
        }
        if(input.getOrganizationId() != null) {
            saveUserInfoDTO.setOrganizationId(input.getOrganizationId());
        }
        if(input.getTeamIdList() != null){
            ArrayList<ObjectId> teamIdList = new ArrayList<>();
            for (ObjectId objectId : input.getTeamIdList()){
                Team team = getTeam(objectId.toString());
                if (team != null && team.getOrganization().getObjectId().equals(input.getOrganizationId())){
                    teamIdList.add(objectId);
                }
            }
            saveUserInfoDTO.setTeamIdList(teamIdList);
        }
        HttpHeaders headers = Utils.generateHttpHeaders(requester, actionId.toHexString());
        HttpEntity<SaveUserInfoDTO> saveUserInfoRequest = new HttpEntity<>(saveUserInfoDTO, headers);
        ResponseDTO<UserInfo> response = new ResponseDTO<UserInfo>();
        boolean errorWhileCreatingUserInfo = false;

        try {
            response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info", saveUserInfoRequest, ResponseDTO.class);
            UserInfo userInfo = objectMapper.convertValue(response.getData(), UserInfo.class);
            if(userInfo == null) {
                errorWhileCreatingUserInfo = true;
            }
            newUser.setUserInfo(userInfo);
            output.generateSuccessResponse(userRepository.save(newUser), "A new user created");
        } catch (ResourceAccessException ex) {
            log.info(ex.getMessage());
            errorWhileCreatingUserInfo = true;
        } catch (HttpClientErrorException ex) {
            log.info(ex.getMessage());
            errorWhileCreatingUserInfo = true;
        }

        if(errorWhileCreatingUserInfo) {
            output.generateErrorResponse(response.getMessage());
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }

    private Team getTeam(String id){
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/team/" + id, HttpMethod.GET, request, ResponseDTO.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Team team = objectMapper.convertValue(response.getBody().getData(), new TypeReference<Team>() { });
        return team;
    }
}
