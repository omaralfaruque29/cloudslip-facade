package com.cloudslip.facade.helper.api_access_token;

import com.cloudslip.facade.dto.*;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.UserType;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class CreateApiAccessTokenHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(CreateApiAccessTokenHelper.class);

    private CreateApiAccessTokenDTO input;
    private ResponseDTO<ApiAccessToken> output = new ResponseDTO<ApiAccessToken>();
    private Optional<ApiAccessToken> existingApiAccessTokenForUser;
    private Optional<User> user;

    @Autowired
    private ApiAccessTokenRepository apiAccessTokenRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void init(BaseInput input, Object... extraParams) {
        this.input = (CreateApiAccessTokenDTO) input;
        this.setOutput(output);
    }


    @Override
    protected void checkPermission() {
        if (requester == null || !requester.hasAuthority(Authority.ROLE_SUPER_ADMIN)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    @Override
    protected void checkValidity() {
        user = userRepository.findById(input.getUserId());
        existingApiAccessTokenForUser = apiAccessTokenRepository.findByUserId(input.getUserId());
        if(!user.isPresent() || !user.get().isValid() || !user.get().isEnabled() || user.get().getUserType().equals(UserType.REGULAR)) {
            output.generateErrorResponse("Cannot create API Access Token for this User");
            throw new ApiErrorException(this.getClass().getName());
        } else if(existingApiAccessTokenForUser.isPresent() && existingApiAccessTokenForUser.get().isValid()) {
            output.generateErrorResponse("An API Access Token already exists for this User");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    @Override
    protected void doPerform() {
        ApiAccessToken apiAccessToken = new ApiAccessToken();
        apiAccessToken.setUser(user.get());
        apiAccessToken.setAccessToken(generateApiAccessToken(user.get()));
        if(input.getAllowedOrigins() != null) {
            apiAccessToken.setAllowedOrigins(input.getAllowedOrigins());
        }
        apiAccessToken.setCreateDate(String.valueOf(LocalDateTime.now()));
        apiAccessToken.setCreateActionId(actionId);
        apiAccessToken.setCreatedBy(requester.getUsername());
        apiAccessToken = apiAccessTokenRepository.save(apiAccessToken);
        output.generateSuccessResponse(apiAccessToken, String.format("API Access Token for %s created successfully", user.get().getUsername()));
    }


    @Override
    protected void postPerformCheck() {

    }


    @Override
    protected void doRollback() {

    }


    private String generateApiAccessToken(User user) {
        return Utils.encodeSha256(user.getId()) + Utils.generateRandomString(10);
    }
}
