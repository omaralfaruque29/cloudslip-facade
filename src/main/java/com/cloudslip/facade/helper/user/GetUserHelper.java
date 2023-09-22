package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GetObjectInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.exception.model.ServiceException;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.AbstractHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetUserHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(GetUserHelper.class);

    private GetObjectInputDTO input;
    private ResponseDTO<User> output = new ResponseDTO<User>();

    @Autowired
    private UserRepository userRepository;


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
        Optional<User> user = userRepository.findById(input.getId());
        if(user.isPresent()) {
            output.generateSuccessResponse(user.get());
        } else {
            output.generateSuccessResponse(null);
        }
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }

}
