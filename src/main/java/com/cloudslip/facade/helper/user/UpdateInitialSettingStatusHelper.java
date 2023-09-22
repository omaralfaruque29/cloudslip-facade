package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.UpdateInitialSettingStatusInputDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.InitialSettingStatus;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UpdateInitialSettingStatusHelper extends AbstractHelper {
    private UpdateInitialSettingStatusInputDTO input;
    private ResponseDTO<User> output = new ResponseDTO<User>();

    private Optional<User> currentUser;

    @Autowired
    private UserRepository userRepository;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (UpdateInitialSettingStatusInputDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {
        if (requester == null || !requester.hasAuthority(Authority.ROLE_ADMIN)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName(), output.getMessage());
        }
    }

    protected void checkValidity() {
        if(requester.getId() == null) {
            output.generateErrorResponse("User id is missing in the params");
            throw new ApiErrorException(this.getClass().getName(), output.getMessage());
        }
        currentUser = userRepository.findById(requester.getObjectId());
        if(!currentUser.isPresent() || !currentUser.get().isValid()) {
            output.generateErrorResponse("User doesn't exists with the given id");
            throw new ApiErrorException(this.getClass().getName(), output.getMessage());
        }
        if(currentUser.get().getInitialSettingStatus() == null || !currentUser.get().getInitialSettingStatus().equals(InitialSettingStatus.PENDING)) {
            output.generateErrorResponse("Cannot update! Current status doesn't support an update.");
            throw new ApiErrorException(this.getClass().getName(), output.getMessage());
        }
    }

    protected void doPerform() {
        currentUser.get().setInitialSettingStatus(input.getInitialSettingStatus());
        userRepository.save(currentUser.get());
        output.generateSuccessResponse(null, "Initial Setting Status Updated!");
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }
}
