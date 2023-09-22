package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.*;
import com.cloudslip.facade.service.RegisterService;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;


@RestController
@RequestMapping("/api")
public class CompanyController {

    private final Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private RegisterService registerService;
    
    @Autowired
    private ApplicationProperties applicationProperties;


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/company/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getCompanyList(@Nullable GetListFilterInput input) throws URISyntaxException {
        log.debug("REST request to get Company List:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/company?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN , ApplicationConstant.ROLE_ADMIN })
    @RequestMapping(value = "/company/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCompany(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get Company: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/company/" + id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/company/create", method = RequestMethod.POST)
    public ResponseEntity<?> createCompany(@RequestBody SaveCompanyDTO dto) {
        log.debug("REST request to create Company: {}", dto);
        ResponseDTO response = registerService.createCompany(dto);
        if(response.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/company/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCompany(@RequestBody UpdateCompanyDTO dto) throws URISyntaxException {
        log.debug("REST request to update Company: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update Company: %s", dto.getName()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateCompanyDTO> request = new HttpEntity<>(dto, headers);
        Object responseObject = restTemplate.putForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/company/" + dto.getId(), request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/company/save-git-info", method = RequestMethod.POST)
    public ResponseEntity<?> saveGitInfo(@RequestBody SaveCompanyGitInfoDTO dto) {
        log.debug("REST request to save Company Github Info: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to add Git Info of Company: %s", dto.getCompanyId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<SaveCompanyGitInfoDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/company/save-git-info", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/company/save-docker-hub-info", method = RequestMethod.POST)
    public ResponseEntity<?> saveDockerHubInfo(@RequestBody SaveCompanyDockerHubInfoInputDTO dto) {
        log.debug("REST request to save Company DockerHub Info: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to add DockerHub Info of this Company"));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<SaveCompanyDockerHubInfoInputDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/company/save-docker-hub-info", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/company/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompany(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to delete Company: {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete Company: %s", id));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/company/" + id, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}


