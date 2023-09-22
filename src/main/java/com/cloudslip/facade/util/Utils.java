package com.cloudslip.facade.util;

import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class Utils {

    public static User getRequester() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User requester = (User) authentication.getDetails();
            return requester;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getRequesterAuthorities() {
        User requester = getRequester();
        return requester.getAuthoritiesAsString();
    }

    public static boolean hasAuthority(User user, Authority authority) {
        return user.getAuthorities().contains(authority);
    }

    public static HttpHeaders generateHttpHeaders() {
        User requester = getRequester();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("current-user", requester.toJsonString());
        return headers;
    }

    public static HttpHeaders generateHttpHeaders(User requester) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("current-user", requester.toJsonString());
        return headers;
    }

    public static HttpHeaders generateHttpHeaders(String actionId) {
        User requester = getRequester();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("current-user", requester != null ? requester.toJsonString() : "");
        headers.add("action-id", actionId);
        return headers;
    }

    public static HttpHeaders generateHttpHeaders(User requester, String actionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("current-user", requester != null ? requester.toJsonString() : "");
        headers.add("action-id", actionId);
        return headers;
    }

    public static String generateRandomNumber(int length) {
        int number = (int) Math.pow(10, length - 1);
        number = number + new Random().nextInt(9 * number);
        return Integer.toString(number);
    }

    public static RequestCallback requestCallback(Serializable updatedInstance) {
        return clientHttpRequest -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(clientHttpRequest.getBody(), updatedInstance);
            clientHttpRequest.getHeaders().add(
                    HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        };
    }

    public static String generateRandomString(int length) {
        String ALLOWED_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( length );
        for( int i = 0; i < length; i++ ) {
            sb.append(ALLOWED_CHARACTERS.charAt(rnd.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String encodeSha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static URI encodeUrl(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        return builder.build().encode().toUri();
    }
}
