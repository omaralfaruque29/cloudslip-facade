package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;


@RestController
@RequestMapping("/api/pipeline")
public class PipelineController {

    private final Logger log = LoggerFactory.getLogger(PipelineController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    

    @RequestMapping(value = "/run-step", method = RequestMethod.POST)
    public ResponseEntity<?> runAppPipelineStep(@RequestBody RunAppPipelineStepInputDTO input) throws URISyntaxException {
        log.debug("REST request to get Region List:");
        SystemAction systemAction = systemActionService.create(String.format("REST request to run app pipeline step: %s", input.getAppCommitPipelineStepId().toHexString()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<RunAppPipelineStepInputDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/pipeline/run-step", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/rollback/{app-pipeline-step-id}", method = RequestMethod.PUT)
    public ResponseEntity<?> rollbackDeployedApplication(@PathVariable("app-pipeline-step-id") String appPipelineStepId) throws URISyntaxException {
        log.debug("REST request to rollback deployed application : {}", appPipelineStepId);
        SystemAction systemAction = systemActionService.create("REST request to rollback deployed application");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameter", headers);
        Object responseObject = restTemplate.putForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/pipeline/rollback/" + appPipelineStepId, request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}


