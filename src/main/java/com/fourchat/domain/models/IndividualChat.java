package com.fourchat.domain.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IndividualChat implements Chat{

    private String id;
    private List<User> participants;
    private List<Message> messages;
    private Date creationDate;
    private int messageCount = 0;
    private List<String> deletedByUsers;

    public IndividualChat( List<User> participants, Date creationDate) {

        this.messages = new ArrayList<>();
        this.creationDate = creationDate;
        this.participants = new ArrayList<>(participants);
        this.deletedByUsers = new ArrayList<>();
    }

    public List<String> getDeletedByUsers() {
        return deletedByUsers;
    }

    @Override
    public void addDeletedByUser(String userId) {
        deletedByUsers.add(userId);
    }

    public void setDeletedByUsers(List<String> deletedByUsers) {
        this.deletedByUsers = deletedByUsers;
    }

    @Override
    public String getGroupName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<User> getAdmins() {
        return List.of();
    }

    @Override
    public int getMessageCount() {
        return 0;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public void addParticipant(User user) {
        participants.add(user);
    }

    @Override
    public void removeParticipant(User user) {

        participants.stream()
                .filter(participant -> participant.getId().equals(user.getId()))
                .findFirst()
                .ifPresent(participants::remove);

    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }


    @Override
    public Message getLastMessage() {
        return messages.getLast();
    }

    @Override
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void addMessage(Message message) {

        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }

        message.setId(String.valueOf(messageCount++));
        this.messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        messages.remove(message);
    }

    @Override
    public void updateMessage(Message messageUpdate) {
        for (int i = 0; i < messages.size(); i++) {
            if (Objects.equals(messages.get(i).getId(), messageUpdate.getId())) {
                messages.set(i, messageUpdate);
                break;
            }
        }
    }

    @Override
    public void notifyParticipants(Message message) {
        for (User user : participants) {
            if (user.equals(message.getSender())) {
                continue;
            }
            user.onMessageReceived(this , message);
        }
    }


}
