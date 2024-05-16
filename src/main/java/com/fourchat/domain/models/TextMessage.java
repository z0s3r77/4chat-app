package com.fourchat.domain.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage implements Message{

    private String id;
    private User sender;
    private String content;
    private Date creationDate;
    private boolean updated = false;
    private boolean deleted = false;

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

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    @Override
    public String toString() {
        return "TextMessage{" +
                "id='" + id + '\'' +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", creationDate=" + creationDate +
                ", updated=" + updated +
                '}';
    }

    @Override
    public boolean getUpdated() {
        return updated;
    }

    @Override
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean getDeleted() {
        return this.deleted;
    }
}
