package com.fourchat.domain.models;

import java.util.List;

public interface User {

    String getId();
    void setId(String id);
    String getUserName();

    String getDescription();
    List<String> getContacts();
    void addContact(String user);
    String getEmail();

    void onMessageReceived(Chat chat, Message message);

}
