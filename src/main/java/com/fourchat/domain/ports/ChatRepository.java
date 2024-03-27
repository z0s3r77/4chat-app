package com.fourchat.domain.ports;

import com.fourchat.domain.models.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    Optional<Chat> findById(String id);
    Chat save(Chat chat);
    boolean delete(Chat chat);
    boolean update(Chat chat);
    List<Chat> findAll();

}