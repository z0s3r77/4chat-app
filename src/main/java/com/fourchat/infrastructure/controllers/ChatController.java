package com.fourchat.infrastructure.controllers;

import com.fourchat.application.NotificationServiceImpl;
import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.controllers.dtos.GroupChatDto;
import com.fourchat.infrastructure.controllers.dtos.MessageDto;
import com.fourchat.infrastructure.controllers.dtos.NewGroupDto;
import com.fourchat.infrastructure.controllers.dtos.SimpleTextMessage;
import com.fourchat.infrastructure.controllers.util.ApiConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ChatController {


    private ChatService chatService;
    private UserService userService;
    private NotificationServiceImpl notificationServiceImpl;

    @GetMapping
    public Chat getChatById(@RequestParam String chatId) {
        return chatService.getChatById(chatId);
    }

    @DeleteMapping("/delete")
    public boolean deleteChat(@RequestParam String chatId, Authentication authentication) {

        Optional<User> user = userService.getUserByUserName(authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return chatService.deleteChat(chatId, user.get().getId());
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




    @PostMapping("/message")
    public Chat sendMessageToChat(@RequestParam String receiver, @RequestBody String content,  Authentication authentication ){

        Optional<User> user = userService.getUserByUserName(authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        TextMessage message = new TextMessage(user.get(), content, new Date());

        return chatService.sendMessage(authentication.getName(), message, receiver );
    }



    // necesitamos el chatId, content , el messageId
    @PostMapping("/message/update")
    public boolean updateMessageFromChat(@RequestBody SimpleTextMessage simpleTextMessage, Authentication authentication ){

        Optional<User> user = userService.getUserByUserName(authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if (!chatService.userIsInChat(simpleTextMessage.getChatId(), user.get().getId())) {
            throw new RuntimeException("User is not in chat");
        }

        Message messageToUpdate = TextMessage.builder()
                .creationDate(simpleTextMessage.getDate())
                .id(simpleTextMessage.getMessageId())
                .content(simpleTextMessage.getContent())
                .build();

        try {
            chatService.updateMessageInChat(simpleTextMessage.getChatId(), messageToUpdate);
            return true;
        } catch (Exception e){
            new RuntimeException(e.getMessage());
            return false;
        }

    }


    @GetMapping("/group")
    public List<GroupChatDto> getGroupChatsFromUser(Authentication authentication) {

        Optional<User> user = userService.getUserByUserName( authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        List<Chat> chats = chatService.getGroupChatsFromUser(user.get().getId());

        return GroupChatDto.from(chats);
    }




    @PostMapping("/group/create")
    public Chat createGroupChat(@RequestBody NewGroupDto groupDto, Authentication authentication) {

        Optional<User> user = userService.getUserByUserName(authentication.getName());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return chatService.createGroupChat(groupDto.participantsIds(), groupDto.groupAdminIds(), groupDto.groupName(), groupDto.description());
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
            ((BasicUser) user).setNotificationService(notificationServiceImpl);
        }

        TextMessage messageText = new TextMessage(user, message.getContent(), new Date());

        var messageSended = chatService.sendMessage(chatId, messageText);

        Chat chat = chatService.getChatById(chatId);

        System.out.println("Message received: " + message.toString());
        System.out.println("Message sended: " + messageSended);


        return chat;
    }


}
