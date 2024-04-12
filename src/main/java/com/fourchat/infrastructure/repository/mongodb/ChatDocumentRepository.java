package com.fourchat.infrastructure.repository.mongodb;

import com.fourchat.infrastructure.repository.documents.ChatDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatDocumentRepository extends MongoRepository<ChatDocument, String> {

    @Query("{ 'participants' : ?0 }")
    List<ChatDocument> findByParticipantsContains(String userName);

}
