package com.fourchat.domain.models;

import java.util.Date;

public interface Message {

    public String getId();
    public void setId(String id);
    public User getSender();
    public String getContent();
    public Date getCreationDate();
    void setContent(String content);
    public String toString();
    public boolean getUpdated();
    public void setUpdated(boolean updated);
    public void setDeleted(boolean deleted);
    public boolean getDeleted();
    public void setSender(User sender);
    public void setReceiver(String receiver);
    public String getReceiver();
}
