package com.fourchat.domain.models;

import java.util.Date;

public class TextMessage implements Message{

    private String id;
    private final User sender;
    private String content;
    private final Date creationDate;

    public TextMessage(User sender, String content, Date creationDate) {
        this.sender = sender;
        this.content = content;
        this.creationDate = creationDate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
