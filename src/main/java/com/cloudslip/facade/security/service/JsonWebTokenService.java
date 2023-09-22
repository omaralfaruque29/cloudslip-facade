package com.cloudslip.facade.security.service;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.controller.UserController;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.TokenDTO;
import com.cloudslip.facade.exception.model.ServiceException;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.*;

import static com.cloudslip.facade.enums.Authority.ROLE_SUPER_ADMIN;


@Service
public class JsonWebTokenService implements TokenService {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Value("${jwt.expire.minutes}")
    private String tokenExpTimeFromProperties;

    private static int tokenExpirationTime;

    @Value("${security.token.secret.key}")
    private String tokenKey;

    @Autowired
    private BasicUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    @Override
    public String getToken(final String username, final String password) {
        if (username == null || password == null) {
            return null;
        }

        final User user = (User) userDetailsService.loadUserByUsername(username);
        tokenExpirationTime = Integer.parseInt(tokenExpTimeFromProperties);
        Map<String, Object> tokenData = new HashMap<>();
            if (user.isEnabled() && passwordEncoder.matches(password, user.getPassword())) {
                tokenData.put("clientType", "user");
                tokenData.put("userID", user.getId());
                tokenData.put("username", user.getUsername());
                tokenData.put("token_create_date", LocalDateTime.now());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, tokenExpirationTime);
                tokenData.put("token_expiration_date", calendar.getTime());
                JwtBuilder jwtBuilder = Jwts.builder();
                jwtBuilder.setExpiration(calendar.getTime());
                jwtBuilder.setClaims(tokenData);
                return jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact();
            } else if(!user.isEnabled()) {
                throw new ServiceException("Disabled user!", this.getClass().getName());
            } else {
                throw new ServiceException("Authentication error", this.getClass().getName());
            }
    }

    @Override
    public TokenDTO authenticate(final String username, final String password) {
        if (username == null || password == null) {
            return null;
        }
        final User user = (User) userDetailsService.loadUserByUsername(username);
        if(user.hasAuthority(ROLE_SUPER_ADMIN) && env.getProperty("env.super-admin-login-enabled").equals("false")){
            return null;
        }
        tokenExpirationTime = Integer.parseInt(tokenExpTimeFromProperties);
        Map<String, Object> tokenData = new HashMap<>();
        if (user.isEnabled() && passwordEncoder.matches(password, user.getPassword())) {
            tokenData.put("clientType", "user");
            tokenData.put("userID", user.getId());
            tokenData.put("username", user.getUsername());
            tokenData.put("token_create_date", LocalDateTime.now());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, tokenExpirationTime);
            tokenData.put("token_expiration_date", calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(calendar.getTime());
            jwtBuilder.setClaims(tokenData);
            TokenDTO dto = new TokenDTO();
            dto.setToken(jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact());
            user.setPassword("");
            dto.setUser(user);
            return dto;
        } else if(!user.isEnabled()) {
            throw new ServiceException("Disabled user!", this.getClass().getName());
        } else {
            throw new ServiceException("Authentication error", this.getClass().getName());
        }
    }

    public static void setTokenExpirationTime(final int tokenExpirationTime) {
        JsonWebTokenService.tokenExpirationTime = tokenExpirationTime;
    }
}
