package com.cloudslip.facade.core;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.dto.CreateApiAccessTokenDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.SaveUserInfoDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.Status;
import com.cloudslip.facade.enums.UserType;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.api_access_token.CreateApiAccessTokenHelper;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class OnStartUpInitDataLoader {

    private Logger log = LogManager.getLogger(OnStartUpInitDataLoader.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiAccessTokenRepository apiAccessTokenRepository;

    @Autowired
    private CreateApiAccessTokenHelper createApiAccessTokenHelper;

    @Autowired
    private ApplicationProperties applicationProperties;


    @Value("${env.usermanagement-service.base-url}")
    private String USER_MANAGEMENT_SERVICE_BASE_URL;

    @PostConstruct
    public void init(){
        // init code goes here
        User superAdmin = this.createFirstSuperUser();
        this.createPipelineServiceUser(superAdmin);
    }


    private User createFirstSuperUser() {
        User superAdmin = userRepository.findByUsername("admin@cloudslip.com");
        if(superAdmin != null) {
            log.info("Super Admin Exists");
            log.info("-------------------------------");
        } else {
            superAdmin = new User();
            superAdmin.setId(ObjectId.get());
            superAdmin.setUsername("admin@cloudslip.com");
            superAdmin.setPassword(passwordEncoder.encode("1234"));
            superAdmin.setVerificationCode(Utils.generateRandomNumber(6));
            superAdmin.setAccountNonExpired(false);
            superAdmin.setCredentialsNonExpired(false);
            superAdmin.setEnabled(true);
            superAdmin.setNeedToResetPassword(false);
            List<Authority> authorities = new ArrayList<>();
            authorities.add(Authority.ROLE_SUPER_ADMIN);
            superAdmin.setAuthorities(authorities);
            superAdmin.setCreateDate(String.valueOf(LocalDateTime.now()));
            log.info("Super Admin Created");
            log.info("-------------------------------");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("current-user", superAdmin.toJsonString());
            HttpEntity<String> request = new HttpEntity<>("parameters", headers);
            ResponseEntity<ResponseDTO> superAdminUserInfo = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/" + superAdmin.getId(), HttpMethod.GET, request, ResponseDTO.class);
            if(superAdminUserInfo.getBody().getStatus().name().equals("success")) {
                log.info("User Info for Super Admin Exists");
                log.info("-------------------------------");
            } else {
                //Create User Info for Super Admin
                this.createFirstSuperUserInfo(superAdmin);
                superAdmin = userRepository.save(superAdmin);
            }

        } catch (ResourceAccessException ex) {
            log.info(ex.getMessage());
        } catch (HttpClientErrorException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                //Create User Info for Super Admin
                this.createFirstSuperUserInfo(superAdmin);
            } else {
                log.info(ex.getMessage());
            }
        }
        return superAdmin;
    }

    private void createFirstSuperUserInfo(User superAdmin) {
        SaveUserInfoDTO saveUserInfoDTO = new SaveUserInfoDTO();
        saveUserInfoDTO.setUserId(superAdmin.getObjectId());
        saveUserInfoDTO.setFirstName("Super");
        saveUserInfoDTO.setLastName("Admin");
        saveUserInfoDTO.setEmail(superAdmin.getUsername());

        boolean errorWhileCreatingUserInfo = false;
        try {
            ResponseDTO<UserInfo> response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/register", saveUserInfoDTO, ResponseDTO.class);
            UserInfo userInfo = objectMapper.convertValue(response.getData(), UserInfo.class);
            if(!response.getStatus().name().equals("success")) {
                errorWhileCreatingUserInfo = true;
            }
            superAdmin.setUserInfo(userInfo);
            log.info("User Info for Super Admin Created");
            log.info("-------------------------------");
        } catch (ResourceAccessException ex) {
            log.info(ex.getMessage());
            errorWhileCreatingUserInfo = true;
        } catch (HttpClientErrorException ex) {
            log.info(ex.getMessage());
            errorWhileCreatingUserInfo = true;
        }

        if(errorWhileCreatingUserInfo) {
            new ResponseDTO().generateErrorResponse("Error to Create UserInfo for Super Admin");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    private void createPipelineServiceUser(User superAdmin) {
        User pipelineServiceUser = userRepository.findByUsername("pipeline-service@cloudslip.com");
        if(pipelineServiceUser != null) {
            log.info("Pipeline Service User Exists");
            log.info("-------------------------------");
        } else {
            pipelineServiceUser = new User();
            pipelineServiceUser.setId(ObjectId.get());
            pipelineServiceUser.setUsername("pipeline-service@cloudslip.com");
            pipelineServiceUser.setPassword(passwordEncoder.encode(""));
            pipelineServiceUser.setVerificationCode(Utils.generateRandomNumber(6));
            pipelineServiceUser.setAccountNonExpired(false);
            pipelineServiceUser.setCredentialsNonExpired(false);
            pipelineServiceUser.setEnabled(true);
            pipelineServiceUser.setNeedToResetPassword(false);
            pipelineServiceUser.setUserType(UserType.AGENT_SERVICE);
            List<Authority> authorities = new ArrayList<>();
            authorities.add(Authority.ROLE_AGENT_SERVICE);
            pipelineServiceUser.setAuthorities(authorities);
            pipelineServiceUser.setCreateDate(String.valueOf(LocalDateTime.now()));
            log.info("Pipeline Service User Created");
            log.info("-------------------------------");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("current-user", superAdmin.toJsonString());
            HttpEntity<String> request = new HttpEntity<>("parameters", headers);
            ResponseEntity<ResponseDTO> superAdminUserInfo = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/" + pipelineServiceUser.getId(), HttpMethod.GET, request, ResponseDTO.class);
            if(superAdminUserInfo.getBody().getStatus().name().equals("success")) {
                log.info("User Info for Pipeline Service User Exists");
                log.info("-------------------------------");
            } else {
                //Create User Info for Pipeline Service User
                this.createPipelineServiceUserInfo(pipelineServiceUser);
                pipelineServiceUser = userRepository.save(pipelineServiceUser);
                createApiAccessTokenForPipelineServiceUser(pipelineServiceUser, superAdmin);

            }

        } catch (ResourceAccessException ex) {
            log.info(ex.getMessage());
        } catch (HttpClientErrorException ex) {
            if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                //Create User Info for Super Admin
                this.createPipelineServiceUserInfo(pipelineServiceUser);
                pipelineServiceUser = userRepository.save(pipelineServiceUser);
                createApiAccessTokenForPipelineServiceUser(pipelineServiceUser, superAdmin);
            } else {
                log.info(ex.getMessage());
            }
        }

    }

    private void createPipelineServiceUserInfo(User pipelineServiceUser) {
        SaveUserInfoDTO saveUserInfoDTO = new SaveUserInfoDTO();
        saveUserInfoDTO.setUserId(pipelineServiceUser.getObjectId());
        saveUserInfoDTO.setFirstName("Pipeline");
        saveUserInfoDTO.setLastName("Service");
        saveUserInfoDTO.setEmail(pipelineServiceUser.getUsername());

        boolean errorWhileCreatingUserInfo = false;
        try {
            ResponseDTO<UserInfo> response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/register", saveUserInfoDTO, ResponseDTO.class);
            UserInfo userInfo = objectMapper.convertValue(response.getData(), UserInfo.class);
            if(!response.getStatus().name().equals("success")) {
                errorWhileCreatingUserInfo = true;
            }
            pipelineServiceUser.setUserInfo(userInfo);
            log.info("User Info for Pipeline Service User Created");
            log.info("-------------------------------");
        } catch (ResourceAccessException ex) {
            log.info(ex.getMessage());
            errorWhileCreatingUserInfo = true;
        } catch (HttpClientErrorException ex) {
            log.info(ex.getMessage());
            errorWhileCreatingUserInfo = true;
        }

        if(errorWhileCreatingUserInfo) {
            new ResponseDTO().generateErrorResponse("Error to Create UserInfo for Pipeline Service User");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    private void createApiAccessTokenForPipelineServiceUser(User pipelineServiceUser, User superAdmin) {
        Optional<ApiAccessToken> apiAccessTokenForPipelineService = apiAccessTokenRepository.findByUserIdAndStatus(pipelineServiceUser.getObjectId(), Status.V);
        if(!apiAccessTokenForPipelineService.isPresent()) {
            CreateApiAccessTokenDTO input = new CreateApiAccessTokenDTO(pipelineServiceUser.getObjectId(), "*");
            createApiAccessTokenHelper.execute(input, superAdmin);
            log.info("Api Access Token for Pipeline Service Created");
            log.info("-------------------------------");
        } else {
            log.info("Api Access Token for Pipeline Service Exists");
            log.info("-------------------------------");
        }
    }
}