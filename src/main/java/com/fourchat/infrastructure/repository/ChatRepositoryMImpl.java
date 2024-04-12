package com.fourchat.infrastructure.repository;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.infrastructure.repository.documents.ChatDocument;
import com.fourchat.infrastructure.repository.mappers.ChatDocumentMapperImpl;
import com.fourchat.infrastructure.repository.mongodb.ChatDocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ChatRepositoryMImpl implements ChatRepository {

    private final ChatDocumentRepository chatDocumentRepository;
    private final ChatDocumentMapperImpl chatDocumentMapper;


    @Override
    public Optional<Chat> findById(String id) {
        return chatDocumentRepository.findById(id).map(chatDocumentMapper::toChat);
    }

    @Override
    public Chat save(Chat chat) {

        ChatDocument chatDocument = chatDocumentMapper.toChatDocument(chat);
        chatDocument = chatDocumentRepository.save(chatDocument);

        return chatDocumentMapper.toChat(chatDocument);

    }

    @Override
    public boolean update(Chat chat) {
        Optional<ChatDocument> existingChatDocument = chatDocumentRepository.findById(chat.getId());

        if (existingChatDocument.isPresent()) {
            ChatDocument updatedChatDocument = chatDocumentMapper.toChatDocument(chat);
            updatedChatDocument.setId(existingChatDocument.get().getId());
            chatDocumentRepository.save(updatedChatDocument);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Chat> findAll() {
        return chatDocumentRepository.findAll().stream()
                .map(chatDocumentMapper::toChat)
                .collect(Collectors.toList());
    }

    @Override
    public List<Chat> findByUserId(String userName) {
        return chatDocumentRepository.findChatDocumentsByParticipantsIdsIn(userName).stream()
                .map(chatDocumentMapper::toChat)
                .collect(Collectors.toList());
    }
}
