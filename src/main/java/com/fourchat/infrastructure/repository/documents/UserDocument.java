package com.fourchat.infrastructure.repository.documents;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "users")
public class UserDocument {


    @Id
    private String id;
    private String userName;
    private String email;
    private String type;
    private String description;
    private List<String> contacts;

}
