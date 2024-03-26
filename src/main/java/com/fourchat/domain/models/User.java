package com.fourchat.domain.models;

public interface User {

    public String getUserName();
    public String getEmail();
    public void onMessageReceived(Chat chat, Message message);

}
