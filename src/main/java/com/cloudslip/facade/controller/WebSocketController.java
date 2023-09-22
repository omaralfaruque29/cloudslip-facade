package com.cloudslip.facade.controller;

import com.cloudslip.facade.dto.WebSocketMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RequestMapping("/api")
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/send/message/all")
    public void onReceiveMessage(@Nullable final String message, @Header(value = "x-auth-token") String authorizationToken) {
        this.template.convertAndSend("/group-chat",LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ": " + message);
    }

    @MessageMapping("/send/message")
    public void sendSpecific(@Payload String msg, Principal user, @Header("target-user") String targetUser) throws Exception {
        template.convertAndSendToUser(targetUser, "/queue/user-message", msg);
    }

    @MessageMapping("/cloudslip-webservice/send/message")
    public void onReceiveAgentMessageToSendToUser(@Nullable final WebSocketMessageDTO message) {
        this.template.convertAndSendToUser(message.getTargetUser(), "/queue/user-message", message.getPayload());
    }

    @MessageMapping("/cloudslip-webservice/broadcast/message")
    public void onReceiveAgentMessageToBroadcast(@Nullable final WebSocketMessageDTO message) {
        this.template.convertAndSend("/topic/" + message.getTargetUser(), message.getPayload());
    }

    @RequestMapping(value = "/test/ws/send/message/{topicName}", method = RequestMethod.GET)
    public void sendTestMessage(@PathVariable("topicName") String topicName) {
        this.template.convertAndSend("/topic/" + topicName,LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ": Hi there - " + topicName);
    }
}
