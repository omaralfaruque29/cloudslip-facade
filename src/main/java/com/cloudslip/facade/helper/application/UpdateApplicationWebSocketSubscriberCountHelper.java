package com.cloudslip.facade.helper.application;

import com.cloudslip.facade.constant.ApplicationProperties;
import com.cloudslip.facade.core.CustomRestTemplate;
import com.cloudslip.facade.dto.BaseInput;
import com.cloudslip.facade.dto.UpdateApplicationWebSocketSubscriberCountInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.enums.Authority;
import com.cloudslip.facade.exception.model.ApiErrorException;
import com.cloudslip.facade.helper.AbstractHelper;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;


@Service
public class UpdateApplicationWebSocketSubscriberCountHelper extends AbstractHelper {

    private final Logger log = LoggerFactory.getLogger(IsUserAllowedToSubscribeToWebSocketTopicHelper.class);

    private UpdateApplicationWebSocketSubscriberCountInputDTO input;
    private ResponseDTO output = new ResponseDTO();

    @Autowired
    private CustomRestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;



    public void init(BaseInput input, Object... extraParams) {
        this.input = (UpdateApplicationWebSocketSubscriberCountInputDTO) input;
        this.setOutput(output);
    }


    protected void checkPermission() {
        if (requester == null || requester.hasAuthority(Authority.ANONYMOUS) || requester.hasAuthority(Authority.ROLE_GIT_AGENT) || requester.hasAuthority(Authority.ROLE_AGENT_SERVICE)) {
            output.generateErrorResponse("Unauthorized user!");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void checkValidity() {
        if(input.getWebSocketTopic() == null) {
            output.generateErrorResponse("Web Socket Topic name is missing in the input");
            throw new ApiErrorException(this.getClass().getName());
        } else if(input.getType() == null) {
            output.generateErrorResponse("Subscriber update type is missing in the input");
            throw new ApiErrorException(this.getClass().getName());
        }
    }


    protected void doPerform() {
        try {
            HttpHeaders headers = Utils.generateHttpHeaders();
            HttpEntity<UpdateApplicationWebSocketSubscriberCountInputDTO> request = new HttpEntity<>(input, headers);
            ResponseDTO responseFromPipeine = restTemplate.postForObject(applicationProperties.getPipelineServiceBaseUrl() + "api/application/web-socket/update-subscriber-count", request, ResponseDTO.class);
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



    protected void postPerformCheck() {
    }

    protected void doRollback() {

    }
}
