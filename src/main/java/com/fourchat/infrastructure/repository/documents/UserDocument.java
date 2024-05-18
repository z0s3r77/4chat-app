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
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private String type;
    private String description;
    private List<String> contacts;
    private String linkedIn;
    private String twitter;
    private String facebook;

}
