package com.fourchat.infrastructure.controllers;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.controllers.dtos.HelloMessage;
import com.fourchat.infrastructure.controllers.util.ApiConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(ApiConstants.CHAT_URL)
public class ChatController {


    private ChatService chatService;
    private UserService userService;


    @GetMapping("/chats")
    public List<Chat> getChatsFromUser(Authentication authentication) {

        Optional<User> user = userService.getUserByUserName(authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return chatService.getChatsFromUser(authentication.getName());
    }


    @MessageMapping("/hello")
    @SendTo("/topic/chat/1")
    public HelloMessage sendMessage(HelloMessage message) {

        System.out.println("Message received: " + message.getName());
        return message;

    }


}
