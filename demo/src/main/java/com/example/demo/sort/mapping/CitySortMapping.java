package com.example.demo.sort.mapping;

import java.util.HashMap;
import java.util.Map;

public class CitySortMapping {
    public static final Map<String, String> FIELD_MAPPING = new HashMap<>();

    static {
        FIELD_MAPPING.put("country", "country.country");
    }
}
