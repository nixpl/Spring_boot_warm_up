package com.example.demo.sort.resolver;

import com.example.demo.annotations.ForCity;
import com.example.demo.sort.mapping.CitySortMapping;

public class CitySortArgumentResolver extends AbstractSortArgumentResolver {

    public CitySortArgumentResolver() {
        super(CitySortMapping.FIELD_MAPPING, ForCity.class);
    }
}
