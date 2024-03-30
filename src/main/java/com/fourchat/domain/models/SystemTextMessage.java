package com.fourchat.domain.models;

import java.util.Date;

public class SystemTextMessage implements  Message{

    private String id;
    private String content;
    private Date creationDate;
    private final User sender;
    private boolean simpleTextMessage = true;


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
    public boolean updated() {
        return false;
    }

    @Override
    public void setUpdated(boolean updated) {
        // TODO document why this method is empty
    }

    @Override
    public String toString() {
        return "SimpleTextMessage{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", creationDate=" + creationDate +
                ", simpleTextMessage=" + simpleTextMessage +
                '}';
    }
}
