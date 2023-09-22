package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.CheckAppEnvStateChecklistDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.service.GenerateAppCommitStateService;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppCommitStateController {

    private final Logger log = LoggerFactory.getLogger(AppCommitStateController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private GenerateAppCommitStateService generateAppCommitStateService;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    

    @RequestMapping(value = "/app-commit-state/generate-state", method = RequestMethod.POST)
    public ResponseEntity<?> initCommitState(@RequestHeader(value = "User-Agent") String userAgent,
                                                           @RequestHeader(value = "X-GitHub-Delivery") String githubDelivery,
                                                           @RequestHeader(value = "X-GitHub-Event") String githubEvent,
                                                           @RequestParam Map<String, String> payloadParam) throws URISyntaxException {
        log.info("REST request to generate App Commit State From Git: {}, {}, {}", userAgent, githubDelivery, githubEvent);
        SystemAction systemAction = systemActionService.create("REST request to generate App Commit State From Git");
        String payload = payloadParam.get("payload");
        String apiAccessToken = payloadParam.get("accessToken");
        String applicationId = payloadParam.get("appId");
        ResponseDTO result = generateAppCommitStateService.getHeader(userAgent, githubDelivery, githubEvent, apiAccessToken, systemAction.getId(), applicationId);
        if (result.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(result.getMessage(), HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = (HttpHeaders) result.getData();
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-commit-state/generate-state", request, ResponseDTO.class);
        if (response.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-commit-state/get-states/{app-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppCommitStates(@PathVariable("app-id") ObjectId appId) throws URISyntaxException {
        log.debug("REST request to get app commit state");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-commit-state/get-states/"+ appId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-commit-state/sync/{app-id}", method = RequestMethod.GET)
    public ResponseEntity<?> syncAppCommitStatesFromGit(@PathVariable("app-id") ObjectId appId) throws URISyntaxException {
        log.debug("REST request to Sync App commit states");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-commit-state/sync/"+ appId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-commit-state/check-app-env-state-checklist", method = RequestMethod.POST)
    public ResponseEntity<?> checkAppEnvStateChecklist(@RequestBody @Valid CheckAppEnvStateChecklistDTO dto) throws URISyntaxException {
        log.debug("REST request to check and uncheck the app env state checklist in app commit state : {}", dto.getAppCommitStateId());
        SystemAction systemAction = systemActionService.create(String.format("REST request to check and uncheck the app env state checklist in app commit state : %s", dto.getAppCommitStateId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CheckAppEnvStateChecklistDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-commit-state/check-app-env-state-checklist", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
