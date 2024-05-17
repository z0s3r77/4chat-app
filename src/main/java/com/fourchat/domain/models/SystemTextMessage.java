package com.fourchat.domain.models;

import java.util.Date;

public class SystemTextMessage implements Message {

    private String id;
    private String content;
    private final Date creationDate;
    private final User sender;


    public SystemTextMessage(String content, Date creationDate) {
        this.sender = new BasicUser("System", "system");
        this.content = content;
        this.creationDate = creationDate;
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public User getSender() {
        return this.sender;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean getUpdated() {
        return false;
    }

    @Override
    public void setUpdated(boolean updated) {
        // System messages cannot be updated
    }

    @Override
    public void setDeleted(boolean deleted) {
        // System messages cannot be deleted
    }

    @Override
    public boolean getDeleted() {
        return false;
    }

    @Override
    public void setSender(User sender) {
        // System messages cannot have a different sender
    }

    @Override
    public String toString() {
        return "SimpleTextMessage{" +
                "id='" + this.id + '\'' +
                ", content='" + this.content + '\'' +
                ", creationDate=" + this.creationDate +
                '}';
    }
}
