package com.cloudslip.facade.security.service;

import com.cloudslip.facade.constant.ApplicationConstant;
import com.cloudslip.facade.enums.UserType;
import com.cloudslip.facade.exception.model.UserNotFoundException;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserAuthentication;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.security.constants.SecurityConstants;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class JsonWebTokenAuthenticationService implements TokenAuthenticationService {

    @Value("${security.token.secret.key}")
    private String secretKey;

    @Autowired
    private BasicUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiAccessTokenRepository apiAccessTokenRepository;


    @Override
    public Authentication authenticate(final HttpServletRequest request) {
        String requestURL = request.getRequestURI();
        String apiAccessToken = null;
        String tempToken = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        if(tempToken == null) {
            tempToken = request.getParameter(SecurityConstants.AUTH_TOKEN_PARAM_NAME);
        }
        if(tempToken == null) {
            apiAccessToken = request.getParameter(SecurityConstants.API_ACCESS_TOKEN_PARAM_NAME);
            if(apiAccessToken != null && isUrlAllowedByApiAccessToken(requestURL)) {
                return authenticateByApiAccessToken(request, apiAccessToken);
            }
        }
        final String token = tempToken;
        final Jws<Claims> tokenData = parseToken(token);
        if (tokenData != null) {
            long expTime = Long.parseLong(tokenData.getBody().get("token_expiration_date").toString());
            if (ifTokenExpired(expTime)) {
                return null;
            }
            User user = getUserFromToken(tokenData);
            if (user != null && user.isValid() && user.getUserType().equals(UserType.REGULAR)) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

    private Authentication authenticateByApiAccessToken(final HttpServletRequest request, final String token) {
        Optional<ApiAccessToken> apiAccessToken = apiAccessTokenRepository.findByAccessToken(token);
        if(apiAccessToken.isPresent()) {
            if(isRequesterOriginAllowed(request, apiAccessToken.get())) {

            }
            Optional<User> user = userRepository.findById(apiAccessToken.get().getUser().getObjectId());
            if(user.isPresent()) {
                return new UserAuthentication(user.get());
            }
            return null;
        }
        return null;
    }

    private boolean ifTokenExpired(long expiration) {
        Date expireDate = new Date(expiration);
        Date currentDate = new Date(System.currentTimeMillis());
        return currentDate.after(expireDate);
    }

    private Jws<Claims> parseToken(final String token) {
        if (token != null) {
            try {
                return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                    | IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private User getUserFromToken(final Jws<Claims> tokenData) {
        try {
            return (User) userDetailsService
                    .loadUserByUsername(tokenData.getBody().get("username").toString());
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("User "
                    + tokenData.getBody().get("username").toString() + " not found");
        }
    }

    public  boolean isUrlAllowedByApiAccessToken(final String url) {
        if(ApplicationConstant.getAllowedUrlsByApiAccessToken().containsKey(url) && ApplicationConstant.getAllowedUrlsByApiAccessToken().get(url) == true) {
            return true;
        } else {
            List<String> allowedUrlAsList = ApplicationConstant.getAllowedUrlAsList();
            for(int i = 0; i < allowedUrlAsList.size(); i++) {
                if(allowedUrlAsList.get(i).length() >= 3) {
                    String lastThreeCharacters = allowedUrlAsList.get(i).substring(allowedUrlAsList.get(i).length() - 3);
                    if(lastThreeCharacters.equals("/**")) {
                        try {
                            String baseUrl = allowedUrlAsList.get(i).substring(0, allowedUrlAsList.get(i).length() - 3);
                            String inputUrlInSameLengthOfBaseUrl = url.substring(0, baseUrl.length());
                            if(baseUrl.equals(inputUrlInSameLengthOfBaseUrl)) {
                                return true;
                            }
                        } catch (StringIndexOutOfBoundsException ex) {
                            //
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isRequesterOriginAllowed(final HttpServletRequest request, ApiAccessToken apiAccessToken) {
        //TODO: check if the origin of the requester is allowed in the apiAccessToken
        return true;
    }
}
