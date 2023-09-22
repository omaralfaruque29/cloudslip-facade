package com.cloudslip.facade.security.service;


import com.cloudslip.facade.dto.TokenDTO;

public interface TokenService {

    String getToken(String username, String password);

    TokenDTO authenticate(String username, String password);
}
