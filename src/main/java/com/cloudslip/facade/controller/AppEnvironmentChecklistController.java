package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.AddAppEnvironmentChecklistDTO;
import com.cloudslip.facade.dto.DeleteChecklistDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.UpdateAppEnvironmentChecklistDTO;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class AppEnvironmentChecklistController {

    private final Logger log = LoggerFactory.getLogger(AppEnvironmentChecklistController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/app-env-checklist/add-checklists", method = RequestMethod.POST)
    public ResponseEntity<?> addChecklistForAppEnvironment(@RequestBody @Valid AddAppEnvironmentChecklistDTO dto) throws URISyntaxException {
        log.debug("REST request to add checklist to application environment : {}", dto.getAppEnvironmentId());
        SystemAction systemAction = systemActionService.create(String.format("REST request to add checklist to application environment : %s", dto.getApplicationId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<AddAppEnvironmentChecklistDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-env-checklist/add-checklists", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/app-env-checklist/update-checklists", method = RequestMethod.POST)
    public ResponseEntity<?> updateChecklistForAppEnvironment(@RequestBody @Valid UpdateAppEnvironmentChecklistDTO dto) throws URISyntaxException {
        log.debug("REST request to update checklist to application environment checklist : {}", dto.getAppEnvChecklistId());
        SystemAction systemAction = systemActionService.create(String.format("REST request to update checklist to application environment checklist : %s", dto.getAppEnvChecklistId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateAppEnvironmentChecklistDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-env-checklist/update-checklists", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/app-env-checklist/get-checklists", method = RequestMethod.GET)
    public ResponseEntity<?> getChecklistForAppEnvironment(@RequestParam("appId") String applicationId, @RequestParam("appEnvId") String appEnvId) throws URISyntaxException {
        log.debug("REST request to get App Environment Checklist by Application and App Environment: {}", applicationId);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-env-checklist/get-checklists?appId=" + applicationId +"&appEnvId=" + appEnvId, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/app-env-checklist/delete-checklists", method = RequestMethod.POST)
    public ResponseEntity<?> deleteChecklistForAppEnvironment(@RequestBody @Valid DeleteChecklistDTO dto) throws URISyntaxException {
        log.debug("REST request to delete App Environment Checklist : {}", dto.getAppEnvChecklistId());
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete app environment checklist : %s", dto.getAppEnvChecklistId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<DeleteChecklistDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-env-checklist/delete-checklists", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
