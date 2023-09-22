package com.cloudslip.facade.controller;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.*;
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
public class TeamController {

    private final Logger log = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private CustomRestTemplate restTemplate;
    
    @Autowired
    private ApplicationProperties applicationProperties;


    
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN, ApplicationConstant.ROLE_DEV, ApplicationConstant.ROLE_OPS})
    @RequestMapping(value = "/team/get-list", method = RequestMethod.GET)
    public ResponseEntity<?> getTeamList(@Nullable GetListFilterInput input) throws URISyntaxException {
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/team?" + input.generateRequestParamUrl(), HttpMethod.GET, request, ResponseDTO.class, input.getFilterParams());
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN, ApplicationConstant.ROLE_DEV, ApplicationConstant.ROLE_OPS})
    @RequestMapping(value = "/team/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTeam(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to get Team: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/team/" + id, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/team/create", method = RequestMethod.POST)
    public ResponseEntity<?> createTeam(@RequestBody SaveTeamDTO dto) {
        log.debug("REST request to create Team: {}", dto);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<SaveTeamDTO> request = new HttpEntity<>(dto, headers);
        ResponseDTO response = restTemplate.postForObject(applicationProperties.getUserManagementServiceBaseUrl() + "api/team", request, ResponseDTO.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/team/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTeam(@RequestBody UpdateTeamDTO dto) throws URISyntaxException {
        log.debug("REST request to update Team: {}", dto);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<UpdateTeamDTO> request = new HttpEntity<>(dto, headers);
        Object responseObject = restTemplate.putForObject( applicationProperties.getUserManagementServiceBaseUrl() + "api/team/" + dto.getId(), request, ResponseDTO.class);
        ResponseDTO response = (ResponseDTO) ((ResponseEntity<?>) responseObject).getBody();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
    @Secured({ApplicationConstant.ROLE_SUPER_ADMIN, ApplicationConstant.ROLE_ADMIN})
    @RequestMapping(value = "/team/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeam(@PathVariable("id") String id) throws URISyntaxException {
        log.debug("REST request to delete Team: {}", id);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/team/" + id, HttpMethod.DELETE, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

}


