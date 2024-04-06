package com.fourchat.infrastructure.controllers;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.controllers.dtos.HelloMessage;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ChatController {


    private ChatService chatService;
    private UserService userService;


    @PostConstruct
    public void init() {
        System.out.println("ChatController ha sido inicializado");
    }


    @PostMapping("/chats/{userName}")
    public List<Chat> getChatsFromUser(@PathVariable String userName) {

        return null;
    }


    @MessageMapping("/hello")
    @SendTo("/topic/chat/1")
    public HelloMessage sendMessage(HelloMessage message) {

        System.out.println("Message received: " + message.getName());
        return message;

    }


}
