package com.cloudslip.facade.controller;


import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.GetLogInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class LogManagementController {

    private final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "/log/fetch", method = RequestMethod.GET)
    public ResponseEntity<?> fetchLog(GetLogInputDTO getLogInputDTO) throws URISyntaxException {
        log.debug("REST request to fetch Log", getLogInputDTO);
        SystemAction systemAction = systemActionService.create(String.format("REST request to fetch log: %s", getLogInputDTO.getAppCommitPipelineStepId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> jenkinsResponse = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/log/fetch?appCommitPipelineStepId=" + getLogInputDTO.getAppCommitPipelineStepId() + "&fetchType=" + getLogInputDTO.getFetchType(), HttpMethod.GET, request, ResponseDTO.class);
        if(getLogInputDTO.getFetchType() == "download"){
            ResponseEntity<?> response;
            try {
                String fileName = "log.txt";
                File file = new File(fileName);
                HttpHeaders responseHeader = new HttpHeaders();
                responseHeader.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
                responseHeader.add("Cache-Control", "no-cache, no-store, must-revalidate");
                responseHeader.add("Pragma", "no-cache");
                responseHeader.add("Expires", "0");
                response = ResponseEntity.ok().headers(responseHeader).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(jenkinsResponse.getBody().getData().toString());
            } catch (Exception e ) {
                return new ResponseEntity<>("download log failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return response;
        } else {
            return new ResponseEntity<>(jenkinsResponse.hasBody() ? jenkinsResponse.getBody() : jenkinsResponse, HttpStatus.OK);
        }
    }

}
