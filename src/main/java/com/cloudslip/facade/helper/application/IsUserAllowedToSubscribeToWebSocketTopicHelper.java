package com.cloudslip.facade.helper.application;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.IsUserAllowedToSubscribeToApplicationWebSocketTopicInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;


@Service
public class IsUserAllowedToSubscribeToWebSocketTopicHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(IsUserAllowedToSubscribeToWebSocketTopicHelper.class);

    private IsUserAllowedToSubscribeToApplicationWebSocketTopicInputDTO input;
    private ResponseDTO<Boolean> output = new ResponseDTO<Boolean>();

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;


    @Override
    public void init(BaseInput input, Object... extraParams) {
        this.input = (IsUserAllowedToSubscribeToApplicationWebSocketTopicInputDTO) input;
        this.setOutput(output);
    }


    @Override
    protected void checkPermission() {
        if (requester == null || requester.hasAuthority(Authority.ROLE_AGENT_SERVICE) || requester.hasAuthority(Authority.ROLE_GIT_AGENT) || requester.hasAuthority(Authority.ANONYMOUS)) {
            log.info("Unauthorized user. No user or invalid user roles to subscribe");
            output.generateErrorResponse(false, "Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    @Override
    protected void checkValidity() {
        if(input.getWebSocketTopic() == null) {
            output.generateErrorResponse(false, "Web Socket Topic is required in input");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    @Override
    protected void doPerform() {
        try {
            HttpHeaders headers = Utils.generateHttpHeaders();
            HttpEntity<String> request = new HttpEntity<>("parameters", headers);
            ResponseEntity<ResponseDTO> response = restTemplate.exchange(applicationProperties.getPipelineServiceBaseUrl() + "api/application/web-socket/is-user-allowed-to-subscribe/" + input.getWebSocketTopic(), HttpMethod.GET, request, ResponseDTO.class);
            ResponseDTO<Boolean> responseFromPipeine = response.hasBody() ? response.getBody() : null;
            if (responseFromPipeine == null) {
                output.generateErrorResponse(false, "Null response from Pipeline");
            } else {
                output.generateSuccessResponse(responseFromPipeine.getData(), responseFromPipeine.getMessage());
            }
        } catch (ResourceAccessException ex) {
            log.error(ex.getMessage());
            output.generateErrorResponse(false, ex.getMessage());
        } catch (HttpClientErrorException ex) {
            log.error(ex.getMessage());
            output.generateErrorResponse(false, ex.getMessage());
        }
    }


    @Override
    protected void postPerformCheck() {

    }


    @Override
    protected void doRollback() {

    }


    private String generateApiAccessToken(User user) {
        return Utils.encodeSha256(user.getId()) + Utils.generateRandomString(10);
    }
}
