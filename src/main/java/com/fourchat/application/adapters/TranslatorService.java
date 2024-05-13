package com.fourchat.application.adapters;

import com.fourchat.domain.ports.Translator;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class TranslatorService implements Translator {

    @Value("${translator.api}")
    private String translatorApi;

    private final RestTemplate restTemplate;

    public TranslatorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {

        if (sourceLanguage.isEmpty()){
            sourceLanguage = "auto";
        }

        try {
            URL url = new URL("http://localhost:5000/translate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{ \"q\": \"%s\", \"source\": \"%s\", \"target\": \"%s\", \"format\": \"text\", \"api_key\": \"\" }", text, sourceLanguage,targetLanguage);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Parse the JSON response using Gson
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            String translatedText = jsonObject.get("translatedText").getAsString();
            conn.disconnect();

            return translatedText;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
