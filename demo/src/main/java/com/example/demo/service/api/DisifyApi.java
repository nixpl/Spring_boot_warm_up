package com.example.demo.service.api;

import com.example.demo.exception.ApiInputOutputException;
import com.example.demo.exception.info.ExceptionInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DisifyApi {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String url;

    public DisifyApi(@Value("${disify.api.url}") String url) {
        this.url = url;
    }

    public boolean isDisposable(String email) {
        Request request = new Request.Builder()
                .url(url + email)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonString = response.body().string();
            JsonNode rootNode = objectMapper.readTree(jsonString);
            return rootNode.get("disposable").asBoolean();
        } catch (IOException e) {
            throw new ApiInputOutputException(ExceptionInfo.DISIFY_API_REQUEST_ERROR, e.getMessage());
        }
    };


}