package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.app_issue_comment.AddAppIssueCommentDTO;
import com.cloudslip.facade.dto.app_issue_comment.UpdateAppIssueCommentDTO;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.service.AppIssueCommentService;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class AppIssueCommentController {

    private final Logger log = LoggerFactory.getLogger(AppIssueCommentController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private AppIssueCommentService appIssueCommentService;

    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(value = "/app-issue-comment/add", method = RequestMethod.POST)
    public ResponseEntity<?> addAppIssueComment(@RequestBody @Valid AddAppIssueCommentDTO input) throws URISyntaxException {
        log.debug("REST request to add comment for an issue for an application : {}", input);
        User requester = Utils.getRequester();
        if (requester != null) {
            ResponseDTO updateDtoResponse = appIssueCommentService.updateAppIssueCommentAddDtoWithTaggedUserList(input, requester);
            if (updateDtoResponse.getStatus() == ResponseStatus.error) {
                return new ResponseEntity<>(updateDtoResponse, HttpStatus.OK);
            }
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            input = objectMapper.convertValue(updateDtoResponse.getData(), new TypeReference<AddAppIssueCommentDTO>() { });
        } else {
            return new ResponseEntity<>(new ResponseDTO<>().generateErrorResponse("Invalid User!"), HttpStatus.OK);
        }
        SystemAction systemAction = systemActionService.create("REST request to add comment for issue for an application");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<AddAppIssueCommentDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue-comment/add", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue-comment/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAppIssueComment(@RequestBody @Valid UpdateAppIssueCommentDTO input) throws URISyntaxException {
        log.debug("REST request to update comment for an app issue : {}", input);
        User requester = Utils.getRequester();
        if (requester != null) {
            ResponseDTO updateDtoResponse = appIssueCommentService.updateAppIssueCommentUpdateDtoWithTaggedUserList(input, requester);
            if (updateDtoResponse.getStatus() == ResponseStatus.error) {
                return new ResponseEntity<>(updateDtoResponse, HttpStatus.OK);
            }
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            input = objectMapper.convertValue(updateDtoResponse.getData(), new TypeReference<UpdateAppIssueCommentDTO>() { });
        } else {
            return new ResponseEntity<>(new ResponseDTO<>().generateErrorResponse("Invalid User!"), HttpStatus.OK);
        }
        SystemAction systemAction = systemActionService.create("REST request to update comment for an app issue");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateAppIssueCommentDTO> request = new HttpEntity<>(input, headers);
        Object responseObject = restTemplate.putForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue-comment/update", request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue-comment/get/{issue-comment-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppIssueComment(@PathVariable("issue-comment-id") String appIssueCommentId) throws URISyntaxException {
        log.debug("REST request to get App Issue Comment : {}", appIssueCommentId);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue-comment/get/"+ appIssueCommentId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue-comment/get-list/{app-issue-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppIssueListByAppIssue(@PathVariable("app-issue-id") String appIssueId) throws URISyntaxException {
        log.debug("REST request to get Comment List By App Issue By App Issue : {}", appIssueId);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue-comment/get-list/"+ appIssueId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-issue-comment/delete/{issue-comment-id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAppIssueComment(@PathVariable("issue-comment-id") String appIssueCommentId) throws URISyntaxException {
        log.debug("REST request to delete application issue comment: {}", appIssueCommentId);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete application issue comment: %s", appIssueCommentId));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/app-issue-comment/delete/" + appIssueCommentId, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
