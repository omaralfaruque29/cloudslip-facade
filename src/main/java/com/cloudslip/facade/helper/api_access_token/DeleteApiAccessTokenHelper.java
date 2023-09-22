package com.cloudslip.facade.helper.api_access_token;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GetObjectInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DeleteApiAccessTokenHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(DeleteApiAccessTokenHelper.class);

    private GetObjectInputDTO input;
    private ResponseDTO output = new ResponseDTO();

    @Autowired
    private ApiAccessTokenRepository apiAccessTokenRepository;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (GetObjectInputDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {
        if (requester == null || (!requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && !requester.hasAuthority(Authority.ROLE_ADMIN))) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void checkValidity() {
        if(input.getId() == null) {
            output.generateErrorResponse("Id is missing in params!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void doPerform() {
        Optional<ApiAccessToken> apiAccessToken = apiAccessTokenRepository.findById(input.getId());
        if(apiAccessToken.isPresent()) {
            apiAccessToken.get().setLastUpdateActionId(actionId);
            apiAccessToken.get().setUpdateDate(String.valueOf(LocalDateTime.now()));
            apiAccessToken.get().setUpdatedBy(requester.getUsername());
            apiAccessTokenRepository.delete(apiAccessToken.get());
            output.generateSuccessResponse(null, "Api access token deleted");
        }
        output.generateErrorResponse("Api access token not found to delete!");
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }

}
