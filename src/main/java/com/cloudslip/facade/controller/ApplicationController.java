package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.dto.application.AddApplicationAdvanceConfigDTO;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.service.ApiAccessTokenService;
import com.cloudslip.facade.service.ApplicationService;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
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
public class ApplicationController {

    private final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApiAccessTokenService apiAccessTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ApplicationService applicationService;
    

    @RequestMapping(value = "/application/create", method = RequestMethod.POST)
    public ResponseEntity<?> createApplication(@RequestBody @Valid CreateApplicationDTO input) throws URISyntaxException {
        log.debug("REST request to create a Application : {}", input);
        SystemAction systemAction = systemActionService.create("REST request to create Application");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CreateApplicationDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/application/create", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateApplication(@RequestBody @Valid UpdateApplicationDTO input) throws URISyntaxException {
        log.debug("REST request to update a Application : {}", input);
        SystemAction systemAction = systemActionService.create("REST request to update Application");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateApplicationDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/application/update", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getApplication(@PathVariable("id") ObjectId id) throws URISyntaxException {
        log.debug("REST request to get Application : {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/application/get/"+ id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getApplicationList(@Nullable GetListFilterInput input, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get Application List");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/application/get-list?"+input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/add-advance-config", method = RequestMethod.POST)
    public ResponseEntity<?> addApplicationAdvanceConfig(@RequestBody @Valid AddApplicationAdvanceConfigDTO dto) throws URISyntaxException {
        log.debug("REST request to add advance config to application and app vpc : {}", dto.getApplicationId());
        SystemAction systemAction = systemActionService.create(String.format("REST request to add advance config to application and app vpc : %s", dto.getApplicationId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<AddApplicationAdvanceConfigDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/application/add-advance-config", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/create-template", method = RequestMethod.POST)
    public ResponseEntity<?> createApplicationTemplate(@RequestBody @Valid CreateApplicationTemplateDTO input) throws URISyntaxException {
        log.debug("REST request to create application template  in jenkins : {}", input);
        SystemAction systemAction = systemActionService.create("REST request to create application template");
        ResponseDTO responseCreateApplicationDTO = apiAccessTokenService.getGitAgentAccessTokenFromCompany(input);
        if (responseCreateApplicationDTO.getStatus() == ResponseStatus.error) {
            systemActionService.saveWithFailure(systemAction, responseCreateApplicationDTO.getMessage());
            return new ResponseEntity<>(responseCreateApplicationDTO, HttpStatus.OK);
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        input = objectMapper.convertValue(responseCreateApplicationDTO.getData(), new TypeReference<CreateApplicationTemplateDTO>() { });
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CreateApplicationTemplateDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/application/create-template", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/delete/{app-id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteApplication(@PathVariable("app-id") String id, @RequestParam("flag") String gitDeleteFlag) throws URISyntaxException {
        log.debug("REST request to delete application: {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete Application: %s", id));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/application/delete/" + id + "?flag=" + gitDeleteFlag, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/trigger-pipeline", method = RequestMethod.POST)
    public ResponseEntity<?> triggerJenkinsJob(@RequestBody TriggerPipelineFromStartInputDTO input) throws URISyntaxException {
        log.debug("Commit ID : {}", input.getCommitId());
        SystemAction systemAction = systemActionService.create(String.format("Commit ID : %s", input));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<?> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-pipeline-step/trigger-pipeline", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/web-socket/is-user-allowed-to-subscribe/{webSocketTopic}", method = RequestMethod.GET)
    public ResponseEntity<?> isUserAllowedToSubscribeToWebSocketTopic(@PathVariable("webSocketTopic") String webSocketTopic) throws URISyntaxException {
        log.debug("REST request to check if user is allowed to subscribe to web socket topic of an application: %s", webSocketTopic);
        boolean isAllowedToSubscribe = applicationService.isUserAllowedToSubscribeToWebSocketTopic(webSocketTopic);
        ResponseDTO response = new ResponseDTO().generateSuccessResponse(isAllowedToSubscribe);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/deployment-status/{app-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppDeploymentStatus(@PathVariable("app-id") String appId) throws URISyntaxException{
        log.debug("REST request to get application deployment status: %s", appId);
        SystemAction systemAction = systemActionService.create(String.format("REST request to get application deployment status: %s", appId));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        System.out.println(request);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/application/deployment-status/" + appId, HttpMethod.GET, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/get-list-by-vpc/{vpc-id}", method = RequestMethod.GET)
    public ResponseEntity<?> getApplicationAndAppVpcListByVpc(@PathVariable("vpc-id") String vpcId) throws URISyntaxException {
        log.debug("REST request to get list of Application by vpc : {}", vpcId);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/application/get-list-by-vpc/"+ vpcId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
