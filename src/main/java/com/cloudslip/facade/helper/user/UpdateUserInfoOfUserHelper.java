package com.cloudslip.facade.helper.user;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.ListInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserInfo;
import com.cloudslip.facade.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UpdateUserInfoOfUserHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(UpdateUserHelper.class);

    private ListInputDTO<UserInfo> input;
    private ResponseDTO output = new ResponseDTO();

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void init(BaseInput input, Object... extraParams) {
        this.input = (ListInputDTO<UserInfo>) input;
        this.setOutput(output);
    }

    @Override
    protected void checkPermission() {
        if (requester == null || (!requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && !requester.hasAuthority(Authority.ROLE_ADMIN))) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }

    @Override
    protected void checkValidity() {

    }

    @Override
    protected void doPerform() {
        List<UserInfo> userInfoList = input.getList();
        for(int i = 0; i < userInfoList.size(); i++) {
            Optional<User> user = userRepository.findById(new ObjectId(userInfoList.get(i).getUserId()));
            if(user.isPresent()) {
                user.get().setUserInfo(userInfoList.get(i));
                userRepository.save(user.get());
            }
        }
        output.generateSuccessResponse(null);
    }

    @Override
    protected void postPerformCheck() {

    }

    @Override
    protected void doRollback() {

    }
}
