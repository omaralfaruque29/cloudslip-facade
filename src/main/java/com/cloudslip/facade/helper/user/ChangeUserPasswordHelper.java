package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.ChangePasswordDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.AbstractHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class ChangeUserPasswordHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(ChangeUserPasswordHelper.class);

    private ChangePasswordDTO input;
    private ResponseDTO<User> output = new ResponseDTO<User>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (ChangePasswordDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {
        if (requester == null || requester.hasAuthority(Authority.ANONYMOUS) || requester.hasAuthority(Authority.ROLE_AGENT_SERVICE)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void checkValidity() {
        if(!passwordEncoder.matches(input.getCurrentPassword(), requester.getPassword())){
            output.generateErrorResponse("Current password didn't match");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void doPerform() {
        requester.setPassword(passwordEncoder.encode(input.getNewPassword()));
        requester.setNeedToResetPassword(false);
        requester = userRepository.save(requester);
        requester.setPassword("");
        output.generateSuccessResponse(requester, "Password changed");
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }
}
