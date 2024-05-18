package com.fourchat.domain.models;

import com.fourchat.domain.ports.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BasicUser implements User {

    private String id;
    private String userName;
    private String email;
    private String description;
    private List<String> contacts;

    @Setter
    private NotificationService notificationService;

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
    public void addContact(String user) {

        if (this.contacts.contains(user)) {
            return;
        }
        this.contacts.add(user);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void onMessageReceived(Chat chat, Message message) {

        if (notificationService == null) {
            System.out.println("Notification sender is null");
            return;
        }

        notificationService.sendNotification(this.getId(), message);
        notificationService.sendNotification(chat, message);
        System.out.println("Chat id : " + chat.getId() + " " + message.getSender().getUserName() + " : " + message.getContent() + "   | id :" + message.getId());
    }


}