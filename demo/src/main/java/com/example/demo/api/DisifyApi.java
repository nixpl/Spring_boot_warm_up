package com.example.demo.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DisifyApi {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean isDisposable(String email) {
        String url = "https://disify.com/api/email/" + email;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JsonNode rootNode = objectMapper.readTree(jsonString);
            return rootNode.get("disposable").asBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    };


}