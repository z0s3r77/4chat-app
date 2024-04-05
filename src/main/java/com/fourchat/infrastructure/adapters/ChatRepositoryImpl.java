package com.fourchat.infrastructure.adapters;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.ports.ChatRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class ChatRepositoryImpl implements ChatRepository {

    private final List<Chat> chats = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger();

    @Override
    public Optional<Chat> findById(String id) {
        return chats.stream()
                .filter(chat -> Objects.equals(chat.getId(), id))
                .findFirst();
    }

    @Override
    public Chat save(Chat chat) {

        Optional<Chat> existingChat = findById(chat.getId());

        if (existingChat.isPresent()) {
            return existingChat.get();
        } else {
            chat.setId(String.valueOf(idCounter.incrementAndGet()));
            chats.add(chat);
            return chat;
        }
    }

    @Override
    public boolean update(Chat chat) {
        for (int i = 0; i < chats.size(); i++) {
            if (Objects.equals(chats.get(i).getId(), chat.getId())) {
                chats.set(i, chat);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Chat> findAll() {
        return chats;
    }

    @Override
    public List<Chat> findByUser(String userName) {
        return chats.stream()
                .filter(chat -> chat.getParticipants().stream().anyMatch(user -> Objects.equals(user.getUserName(), userName)))
                .collect(Collectors.toList());
    }
}