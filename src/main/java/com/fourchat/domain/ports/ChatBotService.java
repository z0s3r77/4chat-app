package com.fourchat.domain.ports;

public interface ChatBotService {

    String getResumeFromChat(String message);
    String getResponseFromQuery(String message);

}
