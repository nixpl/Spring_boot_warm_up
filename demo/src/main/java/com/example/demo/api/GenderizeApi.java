package com.example.demo.api;

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

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JsonNode rootNode = objectMapper.readTree(jsonString);
            String gender = rootNode.get("gender").asText();

            System.out.println("PŁEĆ!!" + gender);

            if (gender.equals("male"))
                return Gender.MALE;
            if (gender.equals("female"))
                return Gender.FEMALE;

            return Gender.UNKNOW;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    };


}
