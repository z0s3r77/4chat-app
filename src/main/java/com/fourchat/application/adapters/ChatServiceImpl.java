package com.fourchat.application.adapters;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.IndividualChat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.ChatService;
import java.util.Collections;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Chat> getChats(User user) {
        return chatRepository.findAll().stream()
                .filter(chat -> chat.getParticipants().contains(user))
                .collect(Collectors.toList());    }

    @Override
    public void removeChat(User user, Chat chat) {
        chat.getParticipants().remove(user);
        chatRepository.save(chat);
    }

    @Override
    public void addChatGroupParticipant(Chat groupChat, User groupAdmin, User participant) {
        // Add the participant to the group chat
    }


    public Chat createIndividualChat(List<User> participants) {

        Chat individualChat = new IndividualChat(participants, new Date());
        return chatRepository.save(individualChat);
    }

    @Override
    public boolean removeMessageFromChat(String chatId, String messageId) {

        Optional<Chat> chatToRemoveMessage = chatRepository.findById(chatId);

        if (chatToRemoveMessage.isPresent()) {

            Chat chat = chatToRemoveMessage.get();
            List<Message> messages = chat.getMessages();
            messages.removeIf(message -> message.getId().equals(messageId));
            return chatRepository.update(chat);
        }

        return false;
    }

    @Override
    public Chat sendMessage(User sender, Message message, User receiver) {

        // Search for an existing chat between the sender and the receiver
        Chat chat = findChat(sender, receiver);

        chat.addMessage(message);
        chat.notifyParticipants(message);

        return chatRepository.save(chat);
    }

    @Override
    public void updateMessageInChat(String chatId, Message messageUpdated) {
        chatRepository.findById(chatId).ifPresent(chat -> {
            chat.getMessages().stream()
                    .filter(message -> message.getId().equals(messageUpdated.getId()))
                    .findFirst()
                    .ifPresent(message -> {
                        message.setContent(messageUpdated.getContent());
                        message.setUpdated(true);
                    });
            chatRepository.update(chat);
        });
    }

    @Override
    public void sendMessage(Chat chat, Message message) {
        // Add the message to the chat
    }

    @Override
    public Chat findChat(User user1, User user2) {
        return chatRepository.findAll().stream()
                .filter(chat -> chat.getParticipants().contains(user1) && chat.getParticipants().contains(user2))
                .findFirst()
                .orElseGet(() -> createIndividualChat(List.of(user1, user2)));
    }

    @Override
    public List<Chat> getUserChats(User user) {
        return Collections.emptyList();
    }
}
