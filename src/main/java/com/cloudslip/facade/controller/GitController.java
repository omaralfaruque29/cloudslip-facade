package com.cloudslip.facade.controller;


import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GitController {

    private final Logger log = LoggerFactory.getLogger(GitController.class);

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "/git/get-repositories", method = RequestMethod.GET)
    public ResponseEntity<?> getGitRepositories() {
        log.debug("REST request to get git repositories");
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/git/get-repositories", HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

    @RequestMapping(value = "/git/get-branches/{repo}", method = RequestMethod.GET)
    public ResponseEntity<?> getGitBranches(@PathVariable("repo") String repo) {
        log.debug("REST request to get git branches", repo);
        HttpHeaders headers = Utils.generateHttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("parameters", headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getUserManagementServiceBaseUrl() + "api/git/get-branches/" + repo, HttpMethod.GET, request, ResponseDTO.class);
        return new ResponseEntity<>(response.hasBody() ? response.getBody() : response, HttpStatus.OK);
    }

}
