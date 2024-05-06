package com.fourchat.infrastructure.controllers;

import com.fourchat.application.adapters.NotificationService;
import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.controllers.dtos.HelloMessage;
import com.fourchat.infrastructure.controllers.dtos.MessageDto;
import com.fourchat.infrastructure.controllers.util.ApiConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(ApiConstants.CHAT_URL)
public class ChatController {


    private ChatService chatService;
    private UserService userService;
    private NotificationService notificationService;

    @GetMapping
    public Chat getChatById(@RequestParam String chatId) {
        return chatService.getChatById(chatId);
    }



    @GetMapping("/chats")
    public List<Chat> getChatsFromUser(Authentication authentication) {

        Optional<User> user = userService.getUserByUserName(authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

       // chatService.sendMessage(authentication.getName(), new TextMessage(user.get(), "Hola Alma", new Date()), "alma.barnichou");

        return chatService.getChatsFromUser(user.get().getId());
    }

    @PostMapping("/delete/message")
    public boolean removeMessageFromChat(@RequestParam String chatId, @RequestParam String messageId, Authentication authentication) {

        Optional<User> user = userService.getUserByUserName(authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if (!chatService.userIsInChat(chatId, user.get().getId())) {
            throw new RuntimeException("User is not in chat");
        }

        return chatService.removeMessageFromChat(chatId, messageId);
    }



    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public Chat sendMessage(@DestinationVariable String chatId, MessageDto message) {

        Optional<User> userOptional = userService.getUserByUserName(message.getSender());

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (user instanceof BasicUser) {
            ((BasicUser) user).setNotificationSender(notificationService);
        }

        TextMessage messageText = new TextMessage(user, message.getContent(), new Date());

        var messageSended = chatService.sendMessage(chatId, messageText);

        Chat chat = chatService.getChatById(chatId);

        System.out.println("Message received: " + message.toString());
        System.out.println("Message sended: " + messageSended);

        return chat;
    }


}
