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
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class AppPipelineStepController {

    private final Logger log = LoggerFactory.getLogger(AppPipelineStepController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;


    @RequestMapping(value = "/app-pipeline-step/add-custom-pipeline-step", method = RequestMethod.POST)
    public ResponseEntity<?> addCustomPipeLineStepsForAppEnv(@RequestBody @Valid AddCustomPipelineStepDTO dto) throws URISyntaxException {
        log.debug("REST request to add custom pipeline step to application environment : {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to add custom pipeline step to application environment : %s", dto.getAppEnvironmentId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<AddCustomPipelineStepDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-pipeline-step/add-custom-pipeline-step", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-pipeline-step/update-pipeline-step-successor", method = RequestMethod.POST)
    public ResponseEntity<?> updateSuccessorForPipelineStep(@RequestBody @Valid UpdatePipelineStepSuccessorDTO dto) throws URISyntaxException {
        log.debug("REST request to update pipeline step successors : {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update pipeline step successors : %s", dto.getPipelineStepId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdatePipelineStepSuccessorDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-pipeline-step/update-pipeline-step-successor", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-pipeline-step/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getPipelineStepsByAppEnv(@Nullable GetListFilterInput input, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get App Pipeline Step list by Application Environment : {}", input);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-pipeline-step/get-list?"+ input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-pipeline-step/update-status", method = RequestMethod.POST)
    public ResponseEntity<?> pipelineRunning(@RequestBody PipelineBuildStatusStartedInputDTO input) throws URISyntaxException {
        SystemAction systemAction = systemActionService.create(String.format("Running ID : %s", input));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<?> request = new HttpEntity<>(input,headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-pipeline-step/update-status", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
