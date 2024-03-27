package com.fourchat.domain.models;

import java.util.Date;

public interface Message {

    public String getId();
    public void setId(String id);
    public User getSender();
    public String getContent();
    public Date getCreationDate();
    void setContent(String content);
}
