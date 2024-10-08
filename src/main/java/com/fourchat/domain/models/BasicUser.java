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
    private String firstName;
    private String lastName;
    private String email;
    private String description;
    private List<String> contacts;
    private String linkedIn;
    private String twitter;
    private String facebook;
    private String photoUrl;
    private List<String> pendingContactRequests;

    @Override
    public List<String> getPendingContactRequests() {
        return pendingContactRequests;
    }

    @Override
    public void addPendingContactRequest(String user) {
        if (this.pendingContactRequests == null) {
            this.pendingContactRequests = new java.util.ArrayList<>();
        }

        if (!this.pendingContactRequests.contains(user)) {
            this.pendingContactRequests.add(user);
        }
    }

    @Override
    public void approveContactRequest(String user) {
        if (this.pendingContactRequests.contains(user)) {
            this.pendingContactRequests.remove(user);
            this.addContact(user);
        }
    }

    @Override
    public void rejectContactRequest(String user) {

        if (this.pendingContactRequests.contains(user)) {
            this.pendingContactRequests.remove(user);
        }

    }


    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }



    @Setter
    private NotificationService notificationService;

    public BasicUser(String userName, String email, String firstName, String lastName) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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
    public String getPhotoUrl() {
        return this.photoUrl;
    }

    @Override
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    @Override
    public void onMessageReceived(Chat chat, Message message) {

        if (notificationService == null) {
            System.out.println("Notification sender is null");
            return;
        }

        if (chat == null) {
            System.out.println("Notificaion for user" + this.getUserName() + " received message from " + message.getSender().getUserName() + " : " + message.getContent() + "   | id :" + message.getId());
            notificationService.sendNotification(this.getId(), message);
            return;
        }

        if (chat instanceof GroupChat) {
            System.out.println("Chat id : " + chat.getId() + " " + message.getSender().getUserName() + " : " + message.getContent() + "   | id :" + message.getId());
            message.setReceiver(chat.getId());
            notificationService.sendNotification(this.getId(), message);
            notificationService.sendNotification(chat, message);
            return;
        }


        notificationService.sendNotification(this.getId(), message);
        notificationService.sendNotification(chat, message);

        System.out.println("Chat id : " + chat.getId() + " " + message.getSender().getUserName() + " : " + message.getContent() + "   | id :" + message.getId());
    }

    @Override
    public boolean removeContact(String userId) {

        return this.contacts.remove(userId);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }


}