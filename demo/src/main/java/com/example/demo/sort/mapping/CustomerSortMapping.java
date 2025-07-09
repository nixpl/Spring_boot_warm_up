package com.example.demo.sort.mapping;

import java.util.HashMap;
import java.util.Map;

public class CustomerSortMapping {
    public static final Map<String, String> FIELD_MAPPING = new HashMap<>();

    static {
        FIELD_MAPPING.put("address", "address.address");
        FIELD_MAPPING.put("address2", "address.address2");
        FIELD_MAPPING.put("district", "address.district");
        FIELD_MAPPING.put("city", "address.city.city");
        FIELD_MAPPING.put("country", "address.city.country.country");
    }
}
