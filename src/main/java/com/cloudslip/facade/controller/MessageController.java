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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private SystemActionService systemActionService;

    @Autowired
    private ApplicationProperties applicationProperties;


    @RequestMapping(value = "/message/send", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageDTO dto) {
        log.debug("REST request to send Message: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to create Message: %s", dto.getSubject()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<SendMessageDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/message/send", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/message/reply", method = RequestMethod.POST)
    public ResponseEntity<?> replyMessage(@RequestBody ReplyMessageDTO dto) {
        log.debug("REST request to send Message: {}", dto);
        SystemAction systemAction = systemActionService.create(String.format("REST request to Reply Message: %s", dto.getMessageThreadId()));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<ReplyMessageDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/message/reply", request, ResponseDTO.class);
        if(response.getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/message/get-all-by-thread/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMessageByThread(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get Message: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/message/get-all-by-thread/" + id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @RequestMapping(value = "/message/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getList(@Nullable GetListFilterInput input) throws URISyntaxException {
        log.debug("REST request to get list of Message Threads:");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        URI uri = Utils.encodeUrl(applicationProperties.getUserManagementServiceBaseUrl() + "api/message/get-list?" + input.generateRequestParamUrl());
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(uri, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }


    @RequestMapping(value = "/message/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMessage(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to delete Message: {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete Message: %s", id));
        HttpHeaders headers = Utils.generateHttpHeaders(systemAction.getId());
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/message/delete/" + id, HttpMethod.DELETE, request, ResponseDTO.class);
        if(response.hasBody() && response.getBody().getStatus() == ResponseStatus.success){
            systemActionService.saveWithSuccess(systemAction);
        } else {
            systemActionService.saveWithFailure(systemAction, response.getBody().getMessage());
        }
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }
}
