package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
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
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class KubeClusterController {

    private final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/kube-cluster/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getKubeClusterList(@Nullable GetListFilterInput input ) throws URISyntaxException {
        log.debug("REST request to get Kube Cluster List:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/kube-cluster/get-list?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/kube-cluster/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getKubeClusterListByEnvironmentId(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get Kube Cluster: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/kube-cluster/get/" + id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/kube-cluster/create", method = RequestMethod.POST)
    public ResponseEntity<?> createKubeCluster(@RequestBody @Valid CreateKubeClusterInputDTO input) {
        log.debug("REST request to create Kube Cluster: {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to create Kube Cluster: %s", input.getName()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<CreateKubeClusterInputDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/kube-cluster/create", request, ResponseDTO.class);
        if(response.getStatus() == com.cloudslip.facade.enums.ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/kube-cluster/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateKubeCluster(@RequestBody @Valid UpdateKubeClusterInputDTO input) throws URISyntaxException {
        log.debug("REST request to update Kube Cluster: {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update Kube Cluster: %s", input.getDefaultNamespace()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<UpdateKubeClusterInputDTO> request = new HttpEntity<>(input, headers);
        Object responseObject = restTemplate.putForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/kube-cluster/update", request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        if(response.getStatus() == com.cloudslip.facade.enums.ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    @RequestMapping(value = "/kube-cluster/delete/{kubeClusterId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteKubeCluster(@PathVariable("kubeClusterId") String kubeClusterId) throws URISyntaxException {
        log.debug("REST request to delete Kube Cluster: {}", kubeClusterId);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete Kube Cluster: %s", kubeClusterId));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = null;
        try {
            response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/kube-cluster/delete/" + kubeClusterId, HttpMethod.DELETE, request, ResponseDTO.class);

            if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
                systemActionService.saveWithSuccess(systemAction);
            } else {
                systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/kube-cluster/get-active-list", method = RequestMethod.GET)
    public ResponseEntity<?> getActiveKubeClusterList(@Nullable GetListFilterInput input ) throws URISyntaxException {
        log.debug("REST request to get active Kube Cluster List:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/kube-cluster/get-active-list?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
