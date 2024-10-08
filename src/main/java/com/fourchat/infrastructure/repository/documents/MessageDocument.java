package com.fourchat.infrastructure.repository.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "messages")
public class MessageDocument {

    private String id;
    private String senderId;
    private String content;
    private Date creationDate;
    private String type;
    private boolean updated = false;

}
