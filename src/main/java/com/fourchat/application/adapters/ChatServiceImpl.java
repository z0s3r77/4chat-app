package com.fourchat.application.adapters;

import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.domain.ports.UserService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final Logger logger = Logger.getLogger(ChatServiceImpl.class.getName());

    private static final String CHAT_IS_NOT_GROUP_CHAT = "Chat with id {0} is not a group chat";
    private static final String USER_IS_NOT_ADMIN = "User {0} is not an admin of the group chat";
    private static final String USER_IS_NOT_PARTICIPANT = "User {0} is not a participant of the group chat";


    public ChatServiceImpl(ChatRepository chatRepository, UserService userService) {
        this.userService = userService;
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Chat> getChatsFromUser(String userName) {
        return this.chatRepository.findByUser(userName);
    }


    private Chat createIndividualChat(List<User> participants) {

        Chat individualChat = new IndividualChat(participants, new Date());
        return this.chatRepository.save(individualChat);
    }

    @Override
    public boolean removeMessageFromChat(String chatId, String messageId) {
        AtomicBoolean messageRemoved = new AtomicBoolean(false);
        this.chatRepository.findById(chatId).ifPresent(chat -> chat.getMessages().stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .ifPresent(message -> {
                    message.setContent("This message has been removed");
                    this.chatRepository.update(chat);
                    messageRemoved.set(true);
                }));
        return messageRemoved.get();
    }

    @Override
    public Chat createGroupChat(List<User> participants, List<User> groupAdmin, String groupName, String description) {

        Chat groupChat = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        return this.chatRepository.save(groupChat);
    }

    @Override
    public boolean updateGroupChatDescription(String chatId, String newDescription) {

        return this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            groupChat.setDescription(newDescription);

            Message message = new SystemTextMessage("Group description updated", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        }).orElse(false);
    }

    @Override
    public boolean updateGroupChatName(String chatId, String newGroupName) {
        return this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            groupChat.setGroupName(newGroupName);
            Message message = new SystemTextMessage("Group title updated", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        }).orElse(false);
    }

    @Override
    public boolean removeParticipantFromGroupChat(String chatId, String adminName, String userName) {

        return this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getUserName().equals(adminName))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminName);
                return false;
            }


            User userToRemove = groupChat.getParticipants().stream()
                    .filter(user -> user.getUserName().equals(userName))
                    .findFirst()
                    .orElse(null);

            if (userToRemove == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_PARTICIPANT, userName);
                return false;
            }

            groupChat.removeParticipant(userToRemove);
            Message message = new SystemTextMessage(userName + " removed from the group", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        }).orElse(false);

    }

    @Override
    public boolean makeParticipantAdmin(String chatId, String adminName, String userName) {

        return this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getUserName().equals(adminName))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminName);
                return false;
            }


            User userToMakeAdmin = groupChat.getParticipants().stream()
                    .filter(user -> user.getUserName().equals(userName))
                    .findFirst()
                    .orElse(null);

            if (userToMakeAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_PARTICIPANT, userName);
                return false;
            }

            groupChat.addAdmin(userToMakeAdmin);
            Message message = new SystemTextMessage(userName + " is new Admin ", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        }).orElse(false);

    }

    @Override
    public boolean removeParticipantFromAdmins(String chatId, String adminName, String userName) {

        return this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getUserName().equals(adminName))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminName);
                return false;
            }


            User userToDeleteFromAdmins = groupChat.getParticipants().stream()
                    .filter(user -> user.getUserName().equals(userName))
                    .findFirst()
                    .orElse(null);

            if (userToDeleteFromAdmins == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_PARTICIPANT, userName);
                return false;
            }

            groupChat.removeAdmin(userToDeleteFromAdmins);
            Message message = new SystemTextMessage(userName + " is removed from admins ", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        }).orElse(false);


    }

    @Override
    public boolean addParticipantToGroupChat(String chatId, String adminName, String userName) {

        return this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getUserName().equals(adminName))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminName);
                return false;
            }

            Optional<User> userToAdd = this.userService.getUserByUserName(userName);

            if (userToAdd.isEmpty()) {
                this.logger.log(Level.WARNING, "User {0} does not exist", userName);
                return false;
            }

            groupChat.addParticipant(userToAdd.get());

            Message message = new SystemTextMessage(userName + " added to the group", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        }).orElse(false);


    }

    @Override
    public Chat sendMessage(User sender, Message message, User receiver) {

        Chat chat = this.findChat(sender, receiver);

        chat.addMessage(message);
        chat.notifyParticipants(message);

        return this.chatRepository.save(chat);
    }

    @Override
    public void updateMessageInChat(String chatId, Message messageUpdated) {
        this.chatRepository.findById(chatId).ifPresent(chat -> {
            chat.getMessages().stream()
                    .filter(message -> message.getId().equals(messageUpdated.getId()))
                    .findFirst()
                    .ifPresent(message -> {
                        message.setContent(messageUpdated.getContent());
                        message.setUpdated(true);
                    });
            this.chatRepository.update(chat);
        });
    }

    @Override
    public boolean sendMessage(String chatId, Message message) {
        return this.chatRepository.findById(chatId).map(chat -> {
            chat.addMessage(message);
            chat.notifyParticipants(message);
            this.chatRepository.save(chat);
            return true;
        }).orElse(false);
    }

    @Override
    public Chat findChat(User user1, User user2) {
        return this.chatRepository.findAll().stream()
                .filter(chat -> chat.getParticipants().contains(user1) && chat.getParticipants().contains(user2))
                .findFirst()
                .orElseGet(() -> this.createIndividualChat(List.of(user1, user2)));
    }


    public Optional<Chat> findChatById(String chatId) {
        return this.chatRepository.findById(chatId);
    }

}
