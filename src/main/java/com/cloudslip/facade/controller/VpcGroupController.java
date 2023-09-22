package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.GetListFilterInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.VpcGroup.CreateVpcGroupDTO;
import com.cloudslip.facade.dto.VpcGroup.UpdateVpcGroupDTO;
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
public class VpcGroupController {

    private final Logger log = LoggerFactory.getLogger(VpcGroupController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/vpc-group/create", method = RequestMethod.POST)
    public ResponseEntity<?> createVpcGroup(@RequestBody @Valid CreateVpcGroupDTO dto) {
        log.debug("REST request to create Vpc Group: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to create Vpc Group: %s", dto));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CreateVpcGroupDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/vpc-group/create", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/vpc-group/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateVpcGroup(@RequestBody @Valid UpdateVpcGroupDTO dto) {
        log.debug("REST request to update Vpc Group: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update Vpc Group: %s", dto));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateVpcGroupDTO> request = new HttpEntity<>(dto, headers);
        Object responseObject = restTemplate.putForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/vpc-group/update", request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/vpc-group/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteVpcGroup(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to delete Vpc Group: {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete Vpc Group: %s", id));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/vpc-group/delete/" + id, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/vpc-group/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getVpcGroupList(@Nullable GetListFilterInput input) throws URISyntaxException {
        log.debug("REST request to get Vpc Group List:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/vpc-group/get-list?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class,  input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/vpc-group/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getVpcGroup(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get Vpc Group: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/vpc-group/get/" + id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
