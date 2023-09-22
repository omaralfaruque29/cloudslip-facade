package com.cloudslip.facade.service;

import com.cloudslip.facade.dto.IsUserAllowedToSubscribeToApplicationWebSocketTopicInputDTO;
import com.cloudslip.facade.dto.ResponseDTO;
import com.cloudslip.facade.dto.UpdateApplicationWebSocketSubscriberCountInputDTO;
import com.cloudslip.facade.enums.UpdateApplicationWebSocketSubscriberCountType;
import com.cloudslip.facade.helper.application.IsUserAllowedToSubscribeToWebSocketTopicHelper;
import com.cloudslip.facade.helper.application.UpdateApplicationWebSocketSubscriberCountHelper;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IsUserAllowedToSubscribeToWebSocketTopicHelper isUserAllowedToSubscribeToWebSocketTopicHelper;

    @Autowired
    private UpdateApplicationWebSocketSubscriberCountHelper updateApplicationWebSocketSubscriberCountHelper;



    /**
     * Is user allowed to subscribe to web socket topic
     *
     * @param webSocketTopic
     * @return boolean
     */
    public boolean isUserAllowedToSubscribeToWebSocketTopic(String webSocketTopic) {
        log.debug("Request to check if user is allowed to subscribe to application web socket topic : {}", webSocketTopic);
        User requester = Utils.getRequester();
        ResponseDTO<Boolean> result = (ResponseDTO<Boolean>) isUserAllowedToSubscribeToWebSocketTopicHelper.execute(new IsUserAllowedToSubscribeToApplicationWebSocketTopicInputDTO(webSocketTopic), requester);
        if(result.getData() == null) {
            return false;
        } else {
            return result.getData();
        }
    }


    /**
     * Is user allowed to subscribe to web socket topic
     *
     * @param webSocketTopic and the requester
     * @return boolean
     */
    public boolean isUserAllowedToSubscribeToWebSocketTopic(String webSocketTopic, User requester) {
        log.debug("Request to check if user is allowed to subscribe to application web socket topic : {}", webSocketTopic);
        ResponseDTO<Boolean> result = (ResponseDTO<Boolean>) isUserAllowedToSubscribeToWebSocketTopicHelper.execute(new IsUserAllowedToSubscribeToApplicationWebSocketTopicInputDTO(webSocketTopic), requester);
        if(result.getData() == null) {
            return false;
        } else {
            return result.getData();
        }
    }


    /**
     * Increase Application Web Socket Topic Subscriber count
     *
     * @param webSocketTopic and the requester
     * @return new subscriber count
     */
    public ResponseDTO increaseWebSocketTopicSubscriberCount(String webSocketTopic, User requester) {
        log.debug("Request to increase subscription count for application web socket topic : {}", webSocketTopic);
        return (ResponseDTO) updateApplicationWebSocketSubscriberCountHelper.execute(new UpdateApplicationWebSocketSubscriberCountInputDTO(webSocketTopic, UpdateApplicationWebSocketSubscriberCountType.INCREASE), requester);
    }


    /**
     * Decrease Application Web Socket Topic Subscriber count
     *
     * @param webSocketTopic and the requester
     * @return new subscriber count
     */
    public ResponseDTO decreaseWebSocketTopicSubscriberCount(String webSocketTopic, User requester) {
        log.debug("Request to decrease subscription count for application web socket topic : {}", webSocketTopic);
        return  (ResponseDTO) updateApplicationWebSocketSubscriberCountHelper.execute(new UpdateApplicationWebSocketSubscriberCountInputDTO(webSocketTopic, UpdateApplicationWebSocketSubscriberCountType.DECREASE), requester);
    }

}
