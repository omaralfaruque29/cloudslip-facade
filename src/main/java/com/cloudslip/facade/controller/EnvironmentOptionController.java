package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.GetListFilterInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.CreateEnvironmentOptionDTO;
import com.cloudslip.facade.dto.UpdateEnvironmentOptionDTO;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api")
public class EnvironmentOptionController {

    private final Logger log = LoggerFactory.getLogger(EnvironmentOptionController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/environment-option/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getEnvironmentOptionList(@Nullable GetListFilterInput input) throws URISyntaxException {
        log.debug("REST request to get EnvironmentOption List:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/environment-option?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/environment-option/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEnvironmentOption(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get EnvironmentOption: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/environment-option/" + id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/environment-option/create", method = RequestMethod.POST)
    public ResponseEntity<?> createEnvironmentOption(@RequestBody @Valid CreateEnvironmentOptionDTO dto) {
        log.debug("REST request to create EnvironmentOption: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to create EnvironmentOption: %s", dto.getName()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CreateEnvironmentOptionDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/environment-option", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/environment-option/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateEnvironmentOption(@RequestBody @Valid UpdateEnvironmentOptionDTO dto) throws URISyntaxException {
        log.debug("REST request to update EnvironmentOption: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update EnvironmentOption: %s", dto.getId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateEnvironmentOptionDTO> request = new HttpEntity<>(dto, headers);
        Object responseObject = restTemplate.putForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/environment-option/update/" + dto.getId(), request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/environment-option/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEnvironmentOption(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to delete EnvironmentOption: {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete EnvironmentOption: %s", id));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/environment-option/delete/" + id, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/active-environment-option/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getActiveEnvironmentOptionList(@Nullable GetListFilterInput input) throws URISyntaxException {
        log.debug("REST request to get active EnvironmentOption List:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/active-environment-option?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}


