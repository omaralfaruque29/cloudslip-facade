package com.cloudslip.facade.helper.api_access_token;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.UpdateApiAccessTokenDTO;
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
public class UpdateApiAccessTokenHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(UpdateApiAccessTokenHelper.class);

    private UpdateApiAccessTokenDTO input;
    private ResponseDTO<ApiAccessToken> output = new ResponseDTO<ApiAccessToken>();
    private Optional<ApiAccessToken> existingApiAccessTokenForUser;

    @Autowired
    private ApiAccessTokenRepository apiAccessTokenRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void init(BaseInput input, Object... extraParams) {
        this.input = (UpdateApiAccessTokenDTO) input;
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
        existingApiAccessTokenForUser = apiAccessTokenRepository.findById(input.getId());
        if(!existingApiAccessTokenForUser.isPresent() || existingApiAccessTokenForUser.get().isDeleted()) {
            output.generateErrorResponse("API Access Token not found!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    @Override
    protected void doPerform() {
        ApiAccessToken apiAccessToken = existingApiAccessTokenForUser.get();
        if(input.getAllowedOrigins() != null) {
            apiAccessToken.setAllowedOrigins(input.getAllowedOrigins());
        }
        apiAccessToken.setUpdateDate(String.valueOf(LocalDateTime.now()));
        apiAccessToken.setLastUpdateActionId(actionId);
        apiAccessToken.setUpdatedBy(requester.getUsername());
        apiAccessToken = apiAccessTokenRepository.save(apiAccessToken);
        output.generateSuccessResponse(apiAccessToken, "API Access Token updated successfully");
    }


    @Override
    protected void postPerformCheck() {

    }


    @Override
    protected void doRollback() {

    }


    private String generateApiAccessToken(User user) {
        return Utils.encodeSha256(user.getId()) + Utils.generateRandomString(15);
    }
}
