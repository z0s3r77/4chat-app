package com.fourchat.application;

import com.fourchat.domain.ports.ChatBotService;
import io.github.stefanbratanov.jvm.openai.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${4chat.chatbot.api}")
    private String API_URL;
    @Value("${4chat.chatbot.apikey}")
    private String API_KEY;


    @Override
    public String getResumeFromChat(String message) {

        return "Hola";
    }


    private String parseResponse(String responseBody) {

        return responseBody + "hola";
    }


    @Override
    public String getResponseFromQuery(String message) {
        return "";
    }
}
