package com.cloudslip.facade.helper.app_commit_state;

import com.cloudslip.facade.config.TopicSubscriptionInterceptor;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.GetHeaderAppCommitStateDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.ApiAccessToken;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.repository.ApiAccessTokenRepository;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class GetHeaderAppCommitStateHelper extends AbstractHelper {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(TopicSubscriptionInterceptor.class);

    private GetHeaderAppCommitStateDTO input;
    private ResponseDTO output = new ResponseDTO();
    private HttpHeaders headers;
    private User gitAgentUser;

    @Autowired
    ApiAccessTokenRepository apiAccessTokenRepository;

    public void init(BaseInput input, Object... extraParams) {
        this.input = (GetHeaderAppCommitStateDTO) input;
        this.setOutput(output);
        this.headers = new HttpHeaders();
    }


    protected void checkPermission() {
        if (input.getApiAccessToken() == null) {
            log.error(this.getClass().getName() + ": Invalid User");
            output.generateErrorResponse("Invalid User!");
            throw new ApiErrorException(this.getClass().getName());
        }
        Optional<ApiAccessToken> accessToken = apiAccessTokenRepository.findByAccessToken(input.getApiAccessToken());
        if (!accessToken.isPresent()) {
            log.error(this.getClass().getName() + ": Invalid Access Token");
            output.generateErrorResponse("Invalid Access Token!");
            throw new ApiErrorException(this.getClass().getName());
        }
        this.gitAgentUser = Utils.getRequester();
    }


    protected void checkValidity() {
        if(Objects.isNull(input.getUserAgent()) || !input.getUserAgent().startsWith("GitHub-Hookshot/") || Objects.isNull(input.getGithubEvent())){
            log.error(this.getClass().getName() + ": Invalid request");
            output.generateErrorResponse("Invalid request!");
            throw new ApiErrorException(this.getClass().getName());
        } else if (!input.getGithubEvent().equals("push")) {
            log.error(this.getClass().getName() + ": No commit requests");
            output.generateErrorResponse("No Commit Requests!");
            throw new ApiErrorException(this.getClass().getName());
        } else if (input.getApplicationId() == null || input.getApplicationId().equals("")) {
            log.error(this.getClass().getName() + ": Application Id Missing");
            output.generateErrorResponse("Application Id Missing!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void doPerform() {
        headers.add("action-id", actionId.toString());
        headers.add("current-user", this.gitAgentUser.toJsonString());
        headers.add("User-Agent", input.getUserAgent());
        headers.add("X-GitHub-Delivery", input.getGithubDelivery());
        headers.add("X-GitHub-Event", input.getGithubEvent());
        headers.add("application-id", input.getApplicationId());
        output.generateSuccessResponse(headers);

    }



    protected void postPerformCheck() {
    }

    protected void doRollback() {

    }
}
