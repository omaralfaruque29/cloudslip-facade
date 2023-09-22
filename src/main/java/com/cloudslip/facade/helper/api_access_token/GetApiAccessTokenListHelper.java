package com.cloudslip.facade.helper.api_access_token;

import com.cloudslip.facade.constant.ListFetchMode;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GetListFilterInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class GetApiAccessTokenListHelper extends AbstractHelper {
    private final Logger log = LoggerFactory.getLogger(GetApiAccessTokenListHelper.class);

    private GetListFilterInput input;
    private ResponseDTO output = new ResponseDTO();
    private Pageable pageable;


    @Autowired
    private ApiAccessTokenRepository apiAccessTokenRepository;


    @Override
    protected void init(BaseInput input, Object... extraParams) {
        this.input = (GetListFilterInput) input;
        this.setOutput(output);
        pageable = (Pageable) extraParams[0];
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

    }


    @Override
    protected void doPerform() {
        if(input.getFetchMode() == null || input.getFetchMode().equals(ListFetchMode.PAGINATION)) {
            output.generateSuccessResponse(apiAccessTokenRepository.findAll(pageable));
        } else if(input.getFetchMode() != null || input.getFetchMode().equals(ListFetchMode.ALL)) {
            output.generateSuccessResponse(apiAccessTokenRepository.findAll());
        } else {
            output.generateErrorResponse("Invalid params in fetch mode");
        }
    }


    @Override
    protected void postPerformCheck() {

    }


    @Override
    protected void doRollback() {

    }
}
