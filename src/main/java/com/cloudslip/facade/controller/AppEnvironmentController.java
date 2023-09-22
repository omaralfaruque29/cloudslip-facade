package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.app_environment.AddAppEnvironmentsDTO;
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
public class AppEnvironmentController {

    private final Logger log = LoggerFactory.getLogger(AppEnvironmentController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    

    @RequestMapping(value = "/app-env/add-environments", method = RequestMethod.POST)
    public ResponseEntity<?> addEnvironmentsOfApplication(@RequestBody @Valid AddAppEnvironmentsDTO dto) throws URISyntaxException {
        log.debug("REST request to add environments to application : {}", dto.getApplicationId());
        SystemAction systemAction = systemActionService.create(String.format("REST request to environments to application : %s", dto.getApplicationId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<AddAppEnvironmentsDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/app-env/add-environments", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/app-env/get-environments", method = RequestMethod.GET)
    public ResponseEntity<?> getAppEnvironmentListByApplication(@Nullable GetListFilterInput input, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get App Environment list by Application: {}", input);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/app-env/get-environments?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
