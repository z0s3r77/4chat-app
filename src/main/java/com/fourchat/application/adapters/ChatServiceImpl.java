package com.fourchat.application.adapters;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.IndividualChat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.ChatService;

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


    }


    public Chat createIndividualChat(List<User> participants) {

        Chat individualChat = new IndividualChat(participants);
        return chatRepository.save(individualChat);
    }

    @Override
    public Chat sendMessage(User sender, Message message, User receiver) {
        Chat chat = findChat(sender, receiver);
        if (chat == null) {
            chat = createIndividualChat(List.of(sender, receiver));
        }
        chat.addMessage(message);
        chat.notifyParticipants(message);
        return chatRepository.save(chat);
    }

    @Override
    public void updateMessageInChat(String chatId, String messageId, Message messageUpdated) {

        Optional<Chat> chatToUpdate = chatRepository.findById(chatId);
        if (chatToUpdate.isPresent()) {
            Chat chat = chatToUpdate.get();
            List<Message> messages = chat.getMessages();
            for (Message message : messages) {
                if (message.getId().equals(messageUpdated.getId())) {
                    message.setContent(messageUpdated.getContent());
                    break;
                }
            }
        }
    }

    @Override
    public void sendMessage(Chat chat, Message message) {

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
        return null;
    }
}
