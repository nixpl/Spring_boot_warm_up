package com.example.demo.sort.mapping;

import java.util.HashMap;
import java.util.Map;

public class AddressSortMapping {
    public static final Map<String, String> FIELD_MAPPING = new HashMap<>();

    static {
        FIELD_MAPPING.put("city", "city.city");
        FIELD_MAPPING.put("country", "city.country.country");
    }
}