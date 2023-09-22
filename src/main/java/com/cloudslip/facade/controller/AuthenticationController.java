package com.cloudslip.facade.controller;

import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.model.SystemAction;
import com.cloudslip.facade.security.service.JsonWebTokenService;
import com.cloudslip.facade.service.RegisterService;
import com.cloudslip.facade.service.SystemActionService;
import com.cloudslip.facade.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JsonWebTokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterService registerService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody final LoginDTO dto) {
        TokenDTO response = tokenService.authenticate(dto.getUsername(), dto.getPassword());
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Authentication failed", HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/change-password", method = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(@RequestBody final ChangePasswordDTO dto) {
        ResponseDTO response = userService.changePassword(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody final SaveCompanyDTO saveCompanyDTO){
        ResponseDTO response = registerService.registerCompany(saveCompanyDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
