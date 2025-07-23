package com.example.demo.api;

import com.example.demo.exception.ApiInputOutputException;
import com.example.demo.exception.info.ExceptionInfo;
import com.example.demo.model.Gender;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GenderizeApi {

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Gender deduceGender(String first_name) {
        String url = "https://api.genderize.io?name=" + first_name;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonString = response.body().string();
            JsonNode rootNode = objectMapper.readTree(jsonString);
            String gender = rootNode.get("gender").asText();

            if (gender.equals("male"))
                return Gender.MALE;
            if (gender.equals("female"))
                return Gender.FEMALE;

            throw new  ApiInputOutputException(ExceptionInfo.UNSUPPORTED_GENDER, gender);

        } catch (IOException e) {
            throw new ApiInputOutputException(ExceptionInfo.GENDERIZE_API_REQUEST_ERROR, e.getMessage());
        }

    };


}
