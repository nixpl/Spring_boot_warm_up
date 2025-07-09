package com.example.demo.sort.resolver;

import com.example.demo.annotations.ForAddress;
import com.example.demo.sort.mapping.AddressSortMapping;

public class AddressSortArgumentResolver extends AbstractSortArgumentResolver {

    public AddressSortArgumentResolver() {
        super(AddressSortMapping.FIELD_MAPPING, ForAddress.class);
    }
}
