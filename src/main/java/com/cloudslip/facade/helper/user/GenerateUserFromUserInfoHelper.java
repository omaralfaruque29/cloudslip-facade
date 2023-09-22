package com.cloudslip.facade.helper.user;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GenerateUserFromUserInfoInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.exception.model.ServiceException;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import com.cloudslip.facade.helper.AbstractHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerateUserFromUserInfoHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(GenerateUserFromUserInfoHelper.class);

    private GenerateUserFromUserInfoInputDTO input;
    private ResponseDTO<User> output = new ResponseDTO<User>();

    private UserInfo userInfo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    protected void init(BaseInput input, Object... extraParams) {
        this.input = (GenerateUserFromUserInfoInputDTO) input;
        this.setOutput(output);
    }

    protected void checkPermission() {
        if (requester == null || (!requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && !requester.hasAuthority(Authority.ROLE_ADMIN))) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void checkValidity() {
        if(input.getUserInfoResponse() == null || input.getUserInfoResponse().getData() ==  null) {
            output.generateErrorResponse("Invalid UserInfo Response provided!");
            throw new ApiErrorException(this.getClass().getName());
        }

        userInfo = objectMapper.convertValue(input.getUserInfoResponse().getData(), UserInfo.class);

        if(userInfo == null || userInfo.getUserId() == null) {
            output.generateErrorResponse("Invalid UserInfo provided!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    protected void doPerform() {
        Optional<User> user = userRepository.findById(new ObjectId(userInfo.getUserId()));
        if(user.isPresent()) {
            user.get().setUserInfo(userInfo);
            output.generateSuccessResponse(user.get());
        } else {
            output.generateErrorResponse("User not found for the given UserInfo!");
        }
    }

    protected void postPerformCheck() {

    }

    protected void doRollback() {

    }

}
