package com.example.demo.api;

import com.example.demo.exception.ApiInputOutputException;
import com.example.demo.exception.info.ExceptionInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DisifyApi {

    private final static OkHttpClient client = new OkHttpClient();
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static boolean isDisposable(String email) {
        String url = "https://disify.com/api/email/" + email;
        Request request = new Request.Builder()
                .url(url)
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