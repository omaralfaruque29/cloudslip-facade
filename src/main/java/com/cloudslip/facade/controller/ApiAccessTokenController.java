package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.enums.ResponseStatus;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.service.ApiAccessTokenService;
import com.cloudslip.facade.service.SystemActionService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class ApiAccessTokenController {

    private final Logger log = LoggerFactory.getLogger(ApiAccessTokenController.class);

    @Autowired
    private ApiAccessTokenService apiAccessTokenService;

    @Autowired
    private SystemActionService systemActionService;


    @RequestMapping(value = "/api-access-token/create", method = RequestMethod.POST)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    public ResponseEntity<?> create(@RequestBody CreateApiAccessTokenDTO input) throws URISyntaxException {
        log.debug("REST request to save User : {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to create API Access Token for User: %s", input.getUserId()));
        ResponseDTO<ApiAccessToken> result = apiAccessTokenService.create(input, systemAction.getObjectId());
        if(result.getStatus() == ResponseStatus.error) {
            systemActionService.saveWithFailure(systemAction, result.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        systemActionService.saveWithSuccess(systemAction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/api-access-token/update", method = RequestMethod.PUT)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    public ResponseEntity<?> update(@RequestBody UpdateUserDTO input) throws URISyntaxException {
        log.debug("REST request to update User : {}", input);
        SystemAction systemAction = systemActionService.create(String.format("REST request to update API Access Token: %s", input.getUserId().toHexString()));
        ResponseDTO result = apiAccessTokenService.update(input, systemAction.getObjectId());
        if(result.getStatus() == ResponseStatus.error) {
            systemActionService.saveWithFailure(systemAction, result.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        systemActionService.saveWithSuccess(systemAction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/api-access-token/{id}", method = RequestMethod.GET)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    public ResponseEntity<?> get(@PathVariable("id") ObjectId id) {
        log.debug("REST request to get Api Access Token : {}", id);
        ResponseDTO result = apiAccessTokenService.findById(id);
        if(result.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "/api-access-token", method = RequestMethod.GET)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    public ResponseEntity<?> getList(@Nullable GetListFilterInput input, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page or list of Api Access Token");
        ResponseDTO result = apiAccessTokenService.findAll(input, pageable);
        if(result.getStatus() == ResponseStatus.error) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "/api-access-token/{id}", method = RequestMethod.DELETE)
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN})
    public ResponseEntity<?> delete(@PathVariable ObjectId id) {
        log.debug("REST request to delete User : {}", id);
        SystemAction systemAction = systemActionService.create(String.format("REST request to delete API Access Token: %s", id.toHexString()));
        ResponseDTO result = apiAccessTokenService.delete(id, systemAction.getObjectId());
        if(result.getStatus() == ResponseStatus.error) {
            systemActionService.saveWithFailure(systemAction, result.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        systemActionService.saveWithSuccess(systemAction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
