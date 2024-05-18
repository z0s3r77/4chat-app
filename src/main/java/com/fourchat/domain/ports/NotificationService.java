package com.fourchat.domain.ports;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;

public interface NotificationService {
    void sendNotification(String userName, Message message);
    void sendNotification(Chat chat, Message message);
}
