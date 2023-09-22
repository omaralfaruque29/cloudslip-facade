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

import java.util.Optional;

@Service
public class GetApiAccessTokenHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(GetApiAccessTokenHelper.class);

    private GetObjectInputDTO input;
    private ResponseDTO<ApiAccessToken> output = new ResponseDTO<ApiAccessToken>();

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
            output.generateSuccessResponse(apiAccessToken.get());
        } else {
            output.generateSuccessResponse(null);
        }
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }

}
