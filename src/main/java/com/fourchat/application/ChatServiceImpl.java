package com.fourchat.application;

import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.controllers.dtos.GroupDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final Logger logger = Logger.getLogger(ChatServiceImpl.class.getName());


    private static final String CHAT_IS_NOT_GROUP_CHAT = "Chat with id {0} is not a group chat";
    private static final String USER_IS_NOT_ADMIN = "User {0} is not an admin of the group chat";
    private static final String USER_IS_NOT_PARTICIPANT = "User {0} is not a participant of the group chat";
    private static final String USER_DOES_NOT_EXIST = "User {0} does not exist";


    public ChatServiceImpl(ChatRepository chatRepository, UserService userService) {
        this.userService = userService;
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat getChatById(String chatId) {
        return this.chatRepository.findById(chatId).orElseThrow(() -> new IllegalArgumentException("Chat not found"));
    }

    @Override
    public List<Chat> getChatsIndexFromUser(String userId) {

        List<Chat> chats = this.chatRepository.findByUserId(userId);

        return chats;
    }

    @Override
    public List<Chat> getChatsFromUser(String userId) {
        return this.chatRepository.findByUserId(userId)
                .stream()
                .filter(chat -> chat.getDeletedByUsers() == null || !chat.getDeletedByUsers().contains(userId))
                .filter(chat -> chat instanceof IndividualChat)
                .collect(Collectors.toList());
    }

    @Override
    public boolean userIsInChat(String chatId, String userId) {

        Optional<Chat> chat = this.chatRepository.findById(chatId);
        return chat.map(value -> value.getParticipants().stream().anyMatch(user -> user.getId().equals(userId))).orElse(false);
    }


    private Chat createIndividualChat(List<String> participantsName) {

        List<User> participantsInChat = participantsName.stream()
                .map(userName -> this.userService.getUserByUserName(userName)
                        .orElseThrow(() -> new IllegalArgumentException(String.format(USER_DOES_NOT_EXIST, userName))))
                .collect(Collectors.toList());

        Chat individualChat = new IndividualChat(participantsInChat, new Date());

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
                    message.setDeleted(true);
                    this.chatRepository.update(chat);
                    messageRemoved.set(true);
                    chat.notifyParticipants(message);
                }));
        return messageRemoved.get();
    }

    @Override
    public boolean deleteChat(String chatId, String userId) {

        return this.chatRepository.findById(chatId).map(chat -> {
            if (chat.getParticipants().stream().anyMatch(user -> user.getId().equals(userId))) {
                chat.addDeletedByUser(userId);
                this.chatRepository.save(chat);
                return true;
            }
            return false;
        }).orElse(false);

    }

    @Override
    public Chat createGroupChat(List<String> participantsIds, List<String> groupAdminIds, String groupName, String description) {

        List<User> participantsInChat = participantsIds.stream()
                .map(id -> this.userService.getUserById(id))
                .collect(Collectors.toList());

        List<User> groupAdmin = groupAdminIds.stream()
                .map(id -> this.userService.getUserById(id))
                .collect(Collectors.toList());

        participantsInChat.addAll(groupAdmin);
        participantsInChat = participantsInChat.stream().distinct().collect(Collectors.toList());

        Chat groupChat = new GroupChat(groupName, description, participantsInChat, groupAdmin, new Date());
        Message message = new SystemTextMessage("Grupo creado", new Date());
        groupChat.addMessage(message);
        groupChat.notifyParticipants(message);

        return this.chatRepository.save(groupChat);
    }

    @Override
    public void updateGroupChatDescription(String chatId, String newDescription) {

        this.chatRepository.findById(chatId).map(chat -> {

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

        });
    }

    @Override
    public void updateGroupChatName(String chatId, String newGroupName) {
        this.chatRepository.findById(chatId).map(chat -> {
            GroupChat groupChat;
            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                throw new IllegalArgumentException("Chat is not a group chat");
            }
            groupChat.setGroupName(newGroupName);
            Message message = new SystemTextMessage("Group title updated", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);
            Chat savedChat = this.chatRepository.save(chat);
            if (savedChat == null) {
                throw new RuntimeException("Failed to save updated group chat");
            }
            return savedChat;
        }).orElseThrow(() -> new IllegalArgumentException("Chat with id " + chatId + " not found"));
    }

    @Override
    public void removeParticipantFromGroupChat(String chatId, String adminId, String userId) {

        this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getId().equals(adminId))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminId);
                return false;
            }


            User userToRemove = groupChat.getParticipants().stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (userToRemove == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_PARTICIPANT, userId);
                return false;
            }

            groupChat.removeParticipant(userToRemove);
            Message message = new SystemTextMessage(userId + " removed from the group", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        });

    }

    @Override
    public void makeParticipantAdmin(String chatId, String adminId, String userId) {

        this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getId().equals(adminId))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminId);
                return false;
            }


            User userToMakeAdmin = groupChat.getParticipants().stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (userToMakeAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_PARTICIPANT, userId);
                return false;
            }

            groupChat.addAdmin(userToMakeAdmin);
            Message message = new SystemTextMessage(userId + " is new Admin ", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        });

    }

    @Override
    public void removeParticipantFromAdmins(String chatId, String adminId, String adminIdToRemove) {

        this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getId().equals(adminId))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminId);
                return false;
            }


            User userToDeleteFromAdmins = groupChat.getParticipants().stream()
                    .filter(user -> user.getId().equals(adminIdToRemove))
                    .findFirst()
                    .orElse(null);

            if (userToDeleteFromAdmins == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_PARTICIPANT, adminIdToRemove);
                return false;
            }

            groupChat.removeAdmin(userToDeleteFromAdmins);
            Message message = new SystemTextMessage(adminIdToRemove + " is removed from admins ", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        });


    }

    @Override
    public void addParticipantToGroupChat(String chatId, String adminId, String userId) {

        this.chatRepository.findById(chatId).map(chat -> {

            GroupChat groupChat;

            try {
                groupChat = (GroupChat) chat;
            } catch (ClassCastException e) {
                this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, chatId);
                return false;
            }

            User userAdmin = groupChat.getAdmins().stream()
                    .filter(user -> user.getId().equals(adminId))
                    .findFirst()
                    .orElse(null);

            if (userAdmin == null) {
                this.logger.log(Level.WARNING, USER_IS_NOT_ADMIN, adminId);
                return false;
            }

            Optional<User> userToAdd = Optional.ofNullable(this.userService.getUserById(userId));

            if (userToAdd.isEmpty()) {
                this.logger.log(Level.WARNING, USER_DOES_NOT_EXIST, userId);
                return false;
            }

            groupChat.addParticipant(userToAdd.get());

            Message message = new SystemTextMessage(userId + " added to the group", new Date());
            groupChat.addMessage(message);
            groupChat.notifyParticipants(message);

            return this.chatRepository.update(chat);

        });
    }


    @Override
    public List<Chat> getGroupChatsFromUser(String userId) {
        logger.info("Fetching group chats for user: " + userId);
        try {
            List<Chat> chats = this.chatRepository.findByUserId(userId).stream()
                    .filter(chat -> chat instanceof GroupChat)
                    .collect(Collectors.toList());
            logger.info("Found " + chats.size() + " group chats for user: " + userId);
            return chats;
        } catch (Exception e) {
            logger.info("Error fetching group chats for user: " + userId + " " + e.getMessage());
            throw e; // O manejar la excepción de otra manera adecuada
        }
    }

    @Override
    public boolean updateGroupChat(GroupDto groupUpdated, String adminId, String groupId) {
        try {
            return Boolean.TRUE.equals(this.chatRepository.findById(groupId).map(chat -> {
                GroupChat groupChat;

                try {
                    groupChat = (GroupChat) chat;
                } catch (ClassCastException e) {
                    this.logger.log(Level.WARNING, CHAT_IS_NOT_GROUP_CHAT, groupId);
                    return false;
                }

                String groupName = chat.getGroupName();
                String groupNameUpdated = groupUpdated.groupName();

                if (!groupName.equals(groupNameUpdated)) {
                    updateGroupChatName(groupId, groupNameUpdated);
                }

                String description = chat.getDescription();
                String descriptionUpdated = groupUpdated.description();

                if (!description.equals(descriptionUpdated)) {
                    updateGroupChatDescription(groupId, descriptionUpdated);
                }

                List<String> currentAdminIds = chat.getAdmins().stream().map(User::getId).toList();
                List<String> updatedAdminIds = groupUpdated.groupAdminIds();

                for (String updatedAdminId : updatedAdminIds) {
                    if (!currentAdminIds.contains(updatedAdminId)) {
                        makeParticipantAdmin(groupId, adminId, updatedAdminId);
                    }
                }

                for (String currentAdminId : currentAdminIds) {
                    if (!updatedAdminIds.contains(currentAdminId)) {
                        removeParticipantFromAdmins(groupId, adminId, currentAdminId);
                    }
                }

                List<String> currentParticipantIds = chat.getParticipants().stream().map(User::getId).toList();
                List<String> updatedParticipantIds = groupUpdated.participantsIds();

                for (String updatedParticipantId : updatedParticipantIds) {
                    if (!currentParticipantIds.contains(updatedParticipantId)) {
                        addParticipantToGroupChat(groupId, adminId, updatedParticipantId);
                    }
                }

                for (String currentParticipantId : currentParticipantIds) {
                    if (!updatedParticipantIds.contains(currentParticipantId)) {
                        removeParticipantFromGroupChat(groupId, adminId, currentParticipantId);
                    }
                }

               return true;
            }).orElse(null));
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, "Error updating group chat", e);
            return false;
        }
    }

    @Override
    public Chat sendMessage(String userNameSender, Message message, String userNameReceiver) {

        User sender = this.userService.getUserByUserName(userNameSender)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_DOES_NOT_EXIST, userNameSender)));

        User receiver = this.userService.getUserByUserName(userNameReceiver).orElseThrow(() -> new IllegalArgumentException(String.format(USER_DOES_NOT_EXIST, userNameReceiver)));

        Chat chat = this.findChat(sender, receiver);

        if (chat.getParticipants().size() == 2) {
            chat.setDeletedByUsers(List.of());
        }

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
                        chat.notifyParticipants(message);
                    });
            this.chatRepository.update(chat);
        });
    }

    @Override
    public boolean sendMessage(String chatId, Message message) {
        return this.chatRepository.findById(chatId).map(chat -> {

            if (chat.getParticipants().size() == 2) {
                chat.setDeletedByUsers(List.of());
            }

            chat.addMessage(message);
            chat.notifyParticipants(message);
            this.chatRepository.save(chat);
            return true;
        }).orElse(false);
    }


    private Chat findChat(User user1, User user2) {
        return this.chatRepository.findAll().stream()
                .filter(chat -> chat.getParticipants().stream().anyMatch(user -> user.getUserName().equals(user1.getUserName()))
                        && chat.getParticipants().stream().anyMatch(user -> user.getUserName().equals(user2.getUserName())))
                .findFirst()
                .orElseGet(() -> this.createIndividualChat(List.of(user1.getUserName(), user2.getUserName())));
    }


    public Optional<Chat> findChatById(String chatId) {
        return this.chatRepository.findById(chatId);
    }

}
