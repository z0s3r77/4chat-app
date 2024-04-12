package com.fourchat.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class BasicUser implements User{

    private String id;
    private String userName;
    private String email;

    public BasicUser(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void onMessageReceived(Chat chat, Message message) {
        System.out.println("Chat id : " + chat.getId() + " " + message.getSender().getUserName() + " : " + message.getContent() + "   | id :" + message.getId());
    }
}