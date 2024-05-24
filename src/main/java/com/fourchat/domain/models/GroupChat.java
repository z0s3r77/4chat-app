package com.fourchat.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@NoArgsConstructor
public class GroupChat implements Chat {

    private String id;
    @Getter
    @Setter
    private String groupName;
    @Getter
    @Setter
    private String description;
    @Setter
    private List<User> participants = new ArrayList<>();
    @Getter
    @Setter
    private List<User> admins = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    @Setter
    private Date creationDate;
    @Setter
    private int messageCount = 0;
    @Getter
    @Setter
    private List<String> deletedByUsers = new ArrayList<>();

    public GroupChat(String groupName, String description, List<User> participants, List<User> admins, Date creationDate) {
        this.groupName = groupName;
        this.description = description;
        this.participants = new ArrayList<>(participants);
        this.admins = new ArrayList<>(admins);
        this.creationDate = creationDate;
        this.deletedByUsers = new ArrayList<>();
    }

    @Override
    public void addDeletedByUser(String userId) {
        deletedByUsers.add(userId);
    }

    @Override
    public int getMessageCount() {
        return this.messageCount;
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
    public List<User> getParticipants() {
        return this.participants;
    }

    @Override
    public void addParticipant(User user) {
        if (!this.participants.contains(user)) {
            this.participants.add(user);
        }
    }

    @Override
    public void removeParticipant(User user) {
        this.participants.remove(user);
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public Message getLastMessage() {
        return this.messages.get(this.messages.size() - 1);
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
        message.setId(String.valueOf(this.messageCount++));
        this.messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    @Override
    public void updateMessage(Message messageUpdate) {
        ListIterator<Message> iterator = this.messages.listIterator();
        while (iterator.hasNext()) {
            Message currentMessage = iterator.next();
            if (Objects.equals(currentMessage.getId(), messageUpdate.getId())) {
                iterator.set(messageUpdate);
                break;
            }
        }
    }

    @Override
    public void notifyParticipants(Message message) {
        this.participants.stream()
                .filter(user -> !user.equals(message.getSender()))
                .forEach(user -> user.onMessageReceived(this, message));
    }

    public void addAdmin(User admin) {
        User adminToAdd = this.participants.stream()
                .filter(user -> user.getId().equals(admin.getId()))
                .findFirst()
                .orElse(null);

        if (adminToAdd != null && !this.admins.contains(adminToAdd)) {
            this.admins.add(adminToAdd);
        }
    }

    public void removeAdmin(User admin) {
        User adminToRemove = this.admins.stream()
                .filter(adminToDelete -> admin.getId().equals(adminToDelete.getId()))
                .findFirst()
                .orElse(null);

        if (adminToRemove != null) {
            this.admins.remove(adminToRemove);
        }
    }
}
