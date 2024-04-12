package com.fourchat.infrastructure.repository.mongodb;

import com.fourchat.infrastructure.repository.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByUserName(String userName);

}
