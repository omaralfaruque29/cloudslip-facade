package com.cloudslip.facade.config;


import com.google.gson.Gson;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Component
public class WebSocketHandlerNonStomp extends TextWebSocketHandler {

    private SimpMessagingTemplate template;

    public WebSocketHandlerNonStomp(SimpMessagingTemplate template) {
        super();
        this.template = template;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage websocketMessage)
            throws InterruptedException, IOException {

        Map value = new Gson().fromJson(websocketMessage.getPayload(), Map.class);
        String targetTopic = (String)value.get("targetTopic");
        String message = (String)value.get("message");
        System.out.println(targetTopic + " - " +  message);

        this.template.convertAndSend("/topic/" + targetTopic, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }
}