package com.fourchat.infrastructure.repository.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "chats")
public class ChatDocument {

    @Id
    private String id;
    private List<String> participantsIds;
    private List<String> messagesIds;
    private Date creationDate;
    private int messageCount = 0;
    private String type;
    private String groupName;
    private String description;
    private List<String> adminsIds;

}
