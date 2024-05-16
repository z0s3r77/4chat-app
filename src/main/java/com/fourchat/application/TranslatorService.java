package com.fourchat.application;

import com.fourchat.domain.ports.Translator;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslatorService implements Translator {

    private final RestTemplate restTemplate;
    private final String translatorApi;

    public TranslatorService(RestTemplate restTemplate, @Value("${translator.api}") String translatorApi) {
        this.restTemplate = restTemplate;
        this.translatorApi = translatorApi;
    }

    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {
        if (sourceLanguage.isEmpty()) {
            sourceLanguage = "auto";
        }

        try {
            String requestBody = createRequestBody(text, sourceLanguage, targetLanguage);
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    translatorApi, HttpMethod.POST, entity, String.class);

            return extractTranslatedText(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to translate text", e);
        }
    }

    private String createRequestBody(String text, String sourceLanguage, String targetLanguage) {
        return String.format("{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\",\"format\":\"text\",\"api_key\":\"\"}",
                text, sourceLanguage, targetLanguage);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private String extractTranslatedText(String responseBody) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        return jsonObject.get("translatedText").getAsString();
    }
}
