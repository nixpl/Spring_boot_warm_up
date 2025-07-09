package com.example.demo.sort.resolver;

import com.example.demo.annotations.ForCustomer;
import com.example.demo.sort.mapping.CustomerSortMapping;

public class CustomerSortArgumentResolver extends AbstractSortArgumentResolver {

    public CustomerSortArgumentResolver() {
        super(CustomerSortMapping.FIELD_MAPPING, ForCustomer.class);
    }
}
