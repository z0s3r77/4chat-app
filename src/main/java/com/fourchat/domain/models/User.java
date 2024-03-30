package com.fourchat.domain.models;

public interface User {

    String getId();
    void setId(String id);
    String getUserName();

    String getEmail();

    void onMessageReceived(Chat chat, Message message);

}
