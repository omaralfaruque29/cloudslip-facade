package com.cloudslip.facade.helper.app_issue;

import com.cloudslip.facade.dto.appissue.CreateAppIssueDTO;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ValidateTaggedUserForCreateAppIssueHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(ValidateTaggedUserForCreateAppIssueHelper.class);

    private CreateAppIssueDTO input;
    private ResponseDTO output = new ResponseDTO();

    private List<User> taggedUserList;

    @Autowired
    private UserRepository userRepository;

    public void init(BaseInput input, Object... extraParams) {
        this.input = (CreateAppIssueDTO) input;
        this.setOutput(output);
        taggedUserList = new ArrayList<>();
    }


    protected void checkPermission() {
        if (requester == null || requester.hasAuthority(Authority.ANONYMOUS) || requester.hasAuthority(Authority.ROLE_AGENT_SERVICE)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void checkValidity() {
    }


    protected void doPerform() {
        if (input.getTaggedUserIdList() != null) {
            for (int index = 0; index < input.getTaggedUserIdList().size(); index++) {
                Optional<User> user = userRepository.findById(input.getTaggedUserIdList().get(index));
                if (!user.isPresent()) {
                    output.generateErrorResponse(String.format("No tagged user found with id -%s!", input.getTaggedUserIdList().get(index)));
                    throw new ApiErrorException(this.getClass().getName());
                }
                /*
                    user cannot tag own self
                    check if user entered same user id multiple times
                 */
                if (!requester.getObjectId().toString().equals(user.get().getObjectId().toString()) && !hasDuplicateUser(user.get())) {
                    taggedUserList.add(user.get());
                }
            }
        }
        input.setTaggedUserList(taggedUserList);
        output.generateSuccessResponse(input);
    }



    protected void postPerformCheck() {
    }

    protected void doRollback() {

    }

    /*
        check if user submitted  same user id multiple times
     */
    private boolean hasDuplicateUser(User inputUser) {
        for (User user : taggedUserList) {
            if (user.getObjectId().toString().equals(inputUser.getObjectId().toString())) {
                return true;
            }
        }
        return false;
    }
}
