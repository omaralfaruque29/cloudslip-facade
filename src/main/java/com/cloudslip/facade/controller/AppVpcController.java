package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.AddCustomIngressDTO;
import com.cloudslip.facade.dto.GetListFilterInput;
import com.cloudslip.facade.dto.ResponseDTO;
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
public class AppVpcController {

    private final Logger log = LoggerFactory.getLogger(AppVpcController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "/app-vpc/add-custom-ingress", method = RequestMethod.POST)
    public ResponseEntity<?> addCustomIngress(@RequestBody @Valid AddCustomIngressDTO input) throws URISyntaxException {
        log.debug("REST request to add custom ingress to app vpc: {}", input);
        SystemAction systemAction = systemActionService.create("REST request to add custom ingress to app vpc");
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<AddCustomIngressDTO> request = new HttpEntity<>(input, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-vpc/add-custom-ingress", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-vpc/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getAppVpcListByApplication(@Nullable GetListFilterInput input, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get App Vpc list by Application: {}", input);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-vpc/get-list?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
