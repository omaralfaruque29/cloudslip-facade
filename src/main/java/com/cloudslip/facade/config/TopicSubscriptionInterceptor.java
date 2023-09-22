package com.cloudslip.facade.config;

import com.cloudslip.facade.enums.Status;
import com.cloudslip.facade.model.User;
import com.cloudslip.facade.model.UserAuthentication;
import com.cloudslip.facade.model.UserWebSocketSession;
import com.cloudslip.facade.repository.UserWebSocketSessionRepository;
import com.cloudslip.facade.service.ApplicationService;
import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;


@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(TopicSubscriptionInterceptor.class);


    private UserWebSocketSessionRepository userWebSocketSessionRepository;

    private ApplicationService applicationService;

    public TopicSubscriptionInterceptor(UserWebSocketSessionRepository userWebSocketSessionRepository, ApplicationService applicationService) {
        this.userWebSocketSessionRepository = userWebSocketSessionRepository;
        this.applicationService = applicationService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            Principal userPrincipal = headerAccessor.getUser();
            if(!validateSubscription(userPrincipal, headerAccessor.getSessionId(), headerAccessor.getDestination())) {
                logger.info("No permission for subscribing to this topic: {}", headerAccessor.getDestination());
                return null;
            }
        } else if(StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand()) || StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
            Principal userPrincipal = headerAccessor.getUser();
            String simpSessionId = headerAccessor.getSessionId();
            this.deRegisterNewWebSocketSession(((UserAuthentication) userPrincipal).getUser(), simpSessionId);
        }
        return message;
    }

    private boolean validateSubscription(Principal principal, String simpSessionId, String topicDestination) {
        if (principal == null) {
            return false;
        }
        logger.info("Validating subscription for {} to topic {}", principal.getName(), topicDestination);
        User user = ((UserAuthentication) principal).getUser();

        //Additional validation logic
        if(isDestinationToApplicationWebSocketTopic(topicDestination)) {
            logger.info("User {} requesting to subscribe to application web socket topic {}", principal.getName(), topicDestination);
            String topicName = this.getTopicNameFromTopicDestination(topicDestination);
            if(applicationService.isUserAllowedToSubscribeToWebSocketTopic(topicName, user)) {
                this.registerNewWebSocketSession(((UserAuthentication) principal).getUser(), simpSessionId, topicDestination);
                applicationService.increaseWebSocketTopicSubscriberCount(topicName, user);
                return true;
            } else  {
                logger.info("User {} is not allowed to subscribe to application web socket topic {}", principal.getName(), topicDestination);
                return false;
            }
        } else {
            this.registerNewWebSocketSession(((UserAuthentication) principal).getUser(), simpSessionId, topicDestination);
            return true;
        }
    }

    private void registerNewWebSocketSession(User user, String simpSessionId, String topicDestination) {
        if(!isWebSocketSessionAlreadyExists(simpSessionId, topicDestination)) {
            UserWebSocketSession userWebSocketSession = new UserWebSocketSession(user.getObjectId(), simpSessionId, topicDestination);
            userWebSocketSessionRepository.save(userWebSocketSession);
        }
    }

    private void deRegisterNewWebSocketSession(User user, String simpSessionId) {
        Optional<UserWebSocketSession> userWebSocketSession = userWebSocketSessionRepository.findByUserIdAndSimpSessionIdAndStatus(user.getObjectId(), simpSessionId, Status.V);
        if(userWebSocketSession.isPresent()) {
            logger.info("Disconnecting web-socket connection for user: {}, session: {}, topic {}", user.getUsername(), userWebSocketSession.get().getSimpSessionId(), userWebSocketSession.get().getWebSocketTopic());
            userWebSocketSessionRepository.delete(userWebSocketSession.get());
            if(isDestinationToApplicationWebSocketTopic(userWebSocketSession.get().getWebSocketTopic())) {
                applicationService.decreaseWebSocketTopicSubscriberCount(getTopicNameFromTopicDestination(userWebSocketSession.get().getWebSocketTopic()), user);
            }
        }
    }

    private boolean isWebSocketSessionAlreadyExists(String simpSessionId, String topic) {
        Optional<UserWebSocketSession> userWebSocketSession = userWebSocketSessionRepository.findBySimpSessionIdAndWebSocketTopicAndStatus(simpSessionId, topic, Status.V);
        return userWebSocketSession.isPresent();
    }

    private boolean isDestinationToApplicationWebSocketTopic(String topicDestination) {
        if(topicDestination.length() > 15 && topicDestination.substring(0, 15).equals("/topic/wst-app-")) {
            return true;
        }
        return false;
    }

    private String getTopicNameFromTopicDestination(String topicDestination) {
        if(topicDestination.length() > 7 && topicDestination.substring(0, 7).equals("/topic/")) {
            return topicDestination.substring(7);
        }
        return null;
    }
}