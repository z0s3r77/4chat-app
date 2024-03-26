package com.fourchat.domain.models;

import java.util.Date;

public interface Message {

    public int getId();
    public void setId(int id);
    public User getSender();
    public Object getContent();
    public Date getCreationDate();

}
