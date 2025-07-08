package com.example.demo.specification;

import com.example.demo.model.Country;
import org.springframework.data.jpa.domain.Specification;

public class CountrySpecifications {

    public static Specification<Country> hasSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String pattern = "%" + searchTerm.toLowerCase() + "%";

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), pattern);
    }
}