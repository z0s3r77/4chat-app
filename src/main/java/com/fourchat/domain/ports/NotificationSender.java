package com.fourchat.domain.ports;

import com.fourchat.domain.models.Message;

public interface NotificationSender {
    void sendNotification(String userName, Message message);
}
