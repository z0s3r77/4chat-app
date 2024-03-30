package com.fourchat.domain.models;

public interface User {

    String getUserName();

    String getEmail();

    void onMessageReceived(Chat chat, Message message);

}
