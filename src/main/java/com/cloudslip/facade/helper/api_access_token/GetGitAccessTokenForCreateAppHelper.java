package com.cloudslip.facade.helper.api_access_token;

import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.CreateApplicationTemplateDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.enums.UserType;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import com.cloudslip.facade.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetGitAccessTokenForCreateAppHelper extends AbstractHelper {

    private CreateApplicationTemplateDTO input;
    private ResponseDTO output = new ResponseDTO();

    @Autowired
    UserRepository  userRepository;

    @Autowired
    ApiAccessTokenRepository apiAccessTokenRepository;

    public void init(BaseInput input, Object... extraParams) {
        this.input = (CreateApplicationTemplateDTO) input;
        this.setOutput(output);
    }


    protected void checkPermission() {
        if (requester == null || requester.hasAuthority(Authority.ANONYMOUS)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void checkValidity() {
        if (requester.hasAuthority(Authority.ROLE_SUPER_ADMIN) && input.getCompanyId() == null) {
            output.generateErrorResponse("Company id is required!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void doPerform() {
        ObjectId companyId;
        if (requester.hasAuthority(Authority.ROLE_SUPER_ADMIN)) {
            companyId = input.getCompanyId();
        } else {
            companyId = requester.getUserInfo().getCompany().getObjectId();
        }
        List<User> gitAgents = userRepository.findAllByUserInfoCompanyObjectIdAndAuthoritiesAuthorityAndUserType(companyId, Authority.ROLE_GIT_AGENT, UserType.GIT);
        if (gitAgents.size() == 0) {
            output.generateErrorResponse("Company does not have any git agent with git user type!");
            throw new ApiErrorException(this.getClass().getName());
        }
        String accessToken = "";
        for (User user : gitAgents) {
            Optional<ApiAccessToken> apiAccessToken = apiAccessTokenRepository.findByUserId(user.getObjectId());
            if (apiAccessToken.isPresent()) {
                accessToken = apiAccessToken.get().getAccessToken();
                break;
            }
        }
        if (accessToken.equals("")) {
            output.generateErrorResponse("API Access Token needs to be created for Company's Git Agent!");
            throw new ApiErrorException(this.getClass().getName());
        }
        input.setGitAgentAccessToken(accessToken);
        output.generateSuccessResponse(input, "Api access token added to dto");
    }



    protected void postPerformCheck() {
    }

    protected void doRollback() {

    }
}
