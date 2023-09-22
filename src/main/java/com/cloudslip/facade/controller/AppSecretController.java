package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.GetListFilterInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.app_secrets.CreateAppSecretDTO;
import com.cloudslip.facade.dto.app_secrets.UpdateAppSecretDTO;
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
public class AppSecretController {

    private final Logger log = LoggerFactory.getLogger(AppSecretController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN,})
    @RequestMapping(value = "/app-secret/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getAppSecretList(@Nullable GetListFilterInput input) throws URISyntaxException {
        log.debug("REST request to get app secret List:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-secret/get-list?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_DEV,ApplicationConstant.ROLE_OPS, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/app-secret/get-list-by-application/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppSecretList(@Nullable GetListFilterInput input, @PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get app secret List By Application:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-secret/"+ id+"/get-list-by-application?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN, ApplicationConstant.ROLE_DEV, ApplicationConstant.ROLE_OPS})
    @RequestMapping(value = "/app-secret/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppSecret(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get app secret: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-secret/get/" + id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-secret/get-app-secret-environment-list/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAppSecretEnvironmentList(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get app secret environment list by application id:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-secret/get-app-secret-environment-list/" +  id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN, ApplicationConstant.ROLE_DEV, ApplicationConstant.ROLE_OPS})
    @RequestMapping(value = "/app-secret/create", method = RequestMethod.POST)
    public ResponseEntity<?> createAppSecret(@RequestBody CreateAppSecretDTO dto) {
        log.debug("REST request to create app secrets: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to create app secret of the application: %s", dto.getApplicationId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CreateAppSecretDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-secret/create", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN ,ApplicationConstant.ROLE_DEV, ApplicationConstant.ROLE_OPS})
    @RequestMapping(value = "/app-secret/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAppSecret(@RequestBody UpdateAppSecretDTO dto) throws URISyntaxException {
        log.debug("REST request to update app secret: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update app secret of the application: %s", dto.getApplicationId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateAppSecretDTO> request = new HttpEntity<>(dto, headers);
        Object responseObject = restTemplate.putForObject( applicationProperties.getPipelineServiceBaseUrl() + "api/app-secret/update", request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN, ApplicationConstant.ROLE_DEV, ApplicationConstant.ROLE_OPS})
    @RequestMapping(value = "/app-secret/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAppSecret(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to delete app secret: {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete app secret: %s", id));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-secret/delete/" + id, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}


