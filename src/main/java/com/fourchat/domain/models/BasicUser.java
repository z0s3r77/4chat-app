package com.fourchat.domain.models;

public class BasicUser implements User{

    private String userName;
    private String email;

    public BasicUser(String userName, String email) {
        this.userName = userName;
        this.email = email;
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