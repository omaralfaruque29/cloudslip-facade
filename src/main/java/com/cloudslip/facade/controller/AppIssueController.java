package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.appissue.CreateAppIssueDTO;
import com.cloudslip.facade.dto.appissue.UpdateAppIssueDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.service.AppIssueService;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class AppIssueController {

    private final Logger log = LoggerFactory.getLogger(AppIssueController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private AppIssueService appIssueService;

    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(value = "/app-issue/create", method = RequestMethod.POST)
    public ResponseEntity<?> createAppIssue(@RequestBody @Valid CreateAppIssueDTO input) throws URISyntaxException {
        log.debug("REST request to create an issue for an application : {}", input);
        User requester = Utils.getRequester();
        if (requester != null) {
            ResponseDTO updateDtoResponse = appIssueService.updateAppIssueCreateDtoWithTaggedUserList(input, requester);
            if (updateDtoResponse.getStatus() == ResponseStatus.error) {
                return new ResponseEntity<>(updateDtoResponse, HttpStatus.OK);
            }
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            input = objectMapper.convertValue(updateDtoResponse.getData(), new TypeReference<CreateAppIssueDTO>() { });
        } else {
            return new ResponseEntity<>(new ResponseDTO<>().generateErrorResponse("Invalid User!"), HttpStatus.OK);
        }
        SystemAction systemAction = systemActionService.create("REST request to create an issue for an application");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CreateAppIssueDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue/create", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateApplicationIssue(@RequestBody @Valid UpdateAppIssueDTO input) throws URISyntaxException {
        log.debug("REST request to update an app issue : {}", input);
        User requester = Utils.getRequester();
        if (requester != null) {
            ResponseDTO updateDtoResponse = appIssueService.updateAppIssueUpdateDtoWithTaggedUserList(input, requester);
            if (updateDtoResponse.getStatus() == ResponseStatus.error) {
                return new ResponseEntity<>(updateDtoResponse, HttpStatus.OK);
            }
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            input = objectMapper.convertValue(updateDtoResponse.getData(), new TypeReference<UpdateAppIssueDTO>() { });
        } else {
            return new ResponseEntity<>(new ResponseDTO<>().generateErrorResponse("Invalid User!"), HttpStatus.OK);
        }
        SystemAction systemAction = systemActionService.create("REST request to update an  app issue");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateAppIssueDTO> request = new HttpEntity<>(input, headers);
        Object responseObject = restTemplate.putForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue/update", request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue/get/{app-issue-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppIssue(@PathVariable("app-issue-id") String appIssueId) throws URISyntaxException {
        log.debug("REST request to get App Issue : {}", appIssueId);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue/get/"+ appIssueId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue/get-list/{app-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppIssueListByApplication(@PathVariable("app-id") String applicationId) throws URISyntaxException {
        log.debug("REST request to get App Issue List By Application : {}", applicationId);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue/get-list/"+ applicationId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue/get-allowed-user-list/{app-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllowedUserForTaggingByApplication(@PathVariable("app-id") String applicationId) throws URISyntaxException {
        log.debug("REST request to get all allowed user list for tagging By Application : {}", applicationId);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue/get-allowed-user-list/"+ applicationId, HttpMethod.GET, request, ResponseDTO.class);
        if (response.getBody().getStatus() == ResponseStatus.success){
            User requester = Utils.getRequester();
            ResponseDTO userListResponse = appIssueService.getAllAllowedUserForTagging(new ObjectId(applicationId), requester, response.getBody());
            return new ResponseEntity<>(userListResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue/delete/{app-issue-id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAppIssue(@PathVariable("app-issue-id") String appIssueId) throws URISyntaxException {
        log.debug("REST request to delete application issue: {}", appIssueId);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete application issue: %s", appIssueId));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue/delete/" + appIssueId, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
