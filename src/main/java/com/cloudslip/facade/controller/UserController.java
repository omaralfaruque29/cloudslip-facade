package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.enums.InitialSettingStatus;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.service.UserService;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ApplicationProperties applicationProperties;


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    public ResponseEntity<?> create(@Valid @RequestBody SaveUserDTO input) throws URISyntaxException {
        log.debug("REST request to save User : {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to create User: %s", input.getUsername()));
        ResponseDTO<User> result = userService.save(input, systemAction.getObjectId());
        if(result.getStatus() == ResponseStatus.error) {
            systemActionService.saveWithFailure(systemAction, result.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        systemActionService.saveWithSuccess(systemAction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "/user/update", method = RequestMethod.PUT)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    public ResponseEntity<?> update(@RequestBody UpdateUserDTO input) throws URISyntaxException {
        log.debug("REST request to update User : {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update User: %s", input.getUserId().toHexString()));
        ResponseDTO result = userService.update(input, systemAction.getObjectId());
        if(result.getStatus() == ResponseStatus.error) {
            systemActionService.saveWithFailure(systemAction, result.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        systemActionService.saveWithSuccess(systemAction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    public ResponseEntity<?> get(@PathVariable("id") ObjectId id, @Nullable GetListFilterInput input, Pageable pageable) {
        log.debug("REST request to get User : {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/" + id , HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        ResponseDTO responseBody = null;
        if(response.hasBody()) {
            responseBody = response.getBody();
            if(responseBody.getStatus() != ResponseStatus.error){
                ResponseDTO finalResponse = userService.generateUserFromUserInfo(new GenerateUserFromUserInfoInputDTO(responseBody));
                return new ResponseEntity<>(finalResponse, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    public ResponseEntity<?> getList(@Nullable GetListFilterInput input, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of User");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        ResponseDTO responseBody = null;
        if(response.hasBody()) {
             responseBody = response.getBody();
             if(responseBody.getStatus() == ResponseStatus.error){
                 return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
             }
             ResponseDTO finalResponse = userService.generateUserListFromUserInfoList(new GenerateUserListFromUserInfoListInputDTO(input, responseBody, pageable));
             return new ResponseEntity<>(finalResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    public ResponseEntity<?> delete(@PathVariable ObjectId id) {
        log.debug("REST request to delete User : {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete User: %s", id.toHexString()));
        ResponseDTO result = userService.delete(id, systemAction.getObjectId());
        if(result.getStatus() == ResponseStatus.error) {
            systemActionService.saveWithFailure(systemAction, result.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        systemActionService.saveWithSuccess(systemAction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/add-users-to-team", method = RequestMethod.POST)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    public ResponseEntity<?> addUsersToTeam(@RequestBody SaveUsersToTeamDTO input) throws URISyntaxException {
        log.debug("REST request to add Users to Team : {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to add Users to Team: %s", input.getTeamId().toHexString()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<SaveUsersToTeamDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/add-users-to-team" , request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            List<UserInfo> userInfoList = objectMapper.convertValue(response.getData(), new TypeReference<List<UserInfo>>() { });
            userService.updateUserInfoOfUser(userInfoList);
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/remove-users-from-team", method = RequestMethod.POST)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    public ResponseEntity<?> removeUsersFromTeam(@RequestBody RemoveUsersFromTeamDTO input) throws URISyntaxException {
        log.debug("REST request to add Users to Team : {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to remove Users to Team: %s", input.getTeamId().toHexString()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<RemoveUsersFromTeamDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/user-info/remove-users-from-team" , request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            List<UserInfo> userInfoList = objectMapper.convertValue(response.getData(), new TypeReference<List<UserInfo>>() { });
            userService.updateUserInfoOfUser(userInfoList);
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getUserList(@Nullable GetListFilterInput input, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of User");
        ResponseDTO result = userService.findAll(input, pageable);
        if(result.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/check-myself-if-active", method = RequestMethod.GET)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN, ApplicationConstant.ROLE_DEV , ApplicationConstant.ROLE_OPS})
    public Boolean getMe() {
        User requester = Utils.getRequester();
        if(requester != null && requester.getStatus().name().equals("V") && requester.isEnabled()){
            return true;
        }else {
            return false;
        }
    }

    @RequestMapping(value = "/user/skip-initial-setting", method = RequestMethod.POST)
    public ResponseEntity<?> skipInitialSettings() {
        log.debug("REST request to Skip Initial Settings");
        ResponseDTO result = userService.updateInitialSettingStatus(new UpdateInitialSettingStatusInputDTO(InitialSettingStatus.SKIPPED));
        if(result.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/complete-initial-setting", method = RequestMethod.POST)
    public ResponseEntity<?> completeInitialSettings() {
        log.debug("REST request to complete initial settings");
        ResponseDTO result = userService.updateInitialSettingStatus(new UpdateInitialSettingStatusInputDTO(InitialSettingStatus.COMPLETED));
        if(result.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
