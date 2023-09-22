package com.cloudslip.facade.config;

import com.cloudslip.facade.repository.UserWebSocketSessionRepository;
import com.cloudslip.facade.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Autowired
    private UserWebSocketSessionRepository userWebSocketSessionRepository;

    @Autowired
    private ApplicationService applicationService;

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpTypeMatchers(SUBSCRIBE, MESSAGE).permitAll();
    }

    public void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new TopicSubscriptionInterceptor(userWebSocketSessionRepository, applicationService));
    }

    @Override
    protected boolean sameOriginDisabled() {
        // While CSRF is disabled..
        return true;
    }

}

