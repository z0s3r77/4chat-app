package com.fourchat.domain.models;

import java.util.Date;

public class TextMessage implements Message{

    private int id;
    private final User sender;
    private final String content;
    private final Date creationDate;

    public TextMessage(User sender, String content, Date creationDate) {
        this.sender = sender;
        this.content = content;
        this.creationDate = creationDate;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public Object getContent() {
        return content;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }
}
