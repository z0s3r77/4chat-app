package com.fourchat.application.adapters;

import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.ChatService;
import java.util.Collections;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final Logger LOGGER = Logger.getLogger(ChatServiceImpl.class.getName());

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


    private Chat createIndividualChat(List<User> participants) {

        Chat individualChat = new IndividualChat(participants, new Date());
        return chatRepository.save(individualChat);
    }

    @Override
    public boolean removeMessageFromChat(String chatId, String messageId) {
        AtomicBoolean messageRemoved = new AtomicBoolean(false);
        chatRepository.findById(chatId).ifPresent(chat -> {
            chat.getMessages().stream()
                    .filter(message -> message.getId().equals(messageId))
                    .findFirst()
                    .ifPresent(message -> {
                        message.setContent("This message has been removed");
                        chatRepository.update(chat);
                        messageRemoved.set(true);
                    });
        });
        return messageRemoved.get();
    }

    @Override
    public Chat createGroupChat(List<User> participants, List<User> groupAdmin, String groupName, String description) {

        Chat groupChat = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        return chatRepository.save(groupChat);
    }

    @Override
    public boolean updateGroupChatDescription(String chatId, String newDescription) {

        return chatRepository.findById(chatId).map( chat -> {

            GroupChat groupChat;

            try{
                 groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                LOGGER.log(Level.WARNING, "Chat with id {0} is not a group chat", chatId);
                return false;
            }

            groupChat.setDescription(newDescription);

            Message message = new SystemTextMessage("Group description updated", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return chatRepository.update(chat);

        }).orElse(false);
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
    public boolean sendMessage(String chatId, Message message) {
        return chatRepository.findById(chatId).map(chat -> {
            chat.addMessage(message);
            chat.notifyParticipants(message);
            chatRepository.save(chat);
            return true;
        }).orElse(false);
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

    public Optional<Chat> findChatById(String chatId) {
        return chatRepository.findById(chatId);
    }

}
