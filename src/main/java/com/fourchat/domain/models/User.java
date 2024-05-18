package com.fourchat.domain.models;

import java.util.List;

public interface User {

    String getId();
    void setId(String id);
    String getUserName();
    String getFirstName();
    String getLastName();
    String getDescription();
    List<String> getContacts();
    void addContact(String user);
    String getEmail();
    String getPhotoUrl();
    void setPhotoUrl(String photoUrl);
    void setDescription(String description);
    String getFacebook();
    String getTwitter();
    String getLinkedIn();
    void setContacts(List<String> contacts);
    void onMessageReceived(Chat chat, Message message);

}
