package com.example.demo.Specification;

import com.example.demo.model.City;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CitySpecifications {

    public static Specification<City> hasSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String pattern = "%" + searchTerm.toLowerCase() + "%";

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("country").get("country")), pattern));
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}