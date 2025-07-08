package com.example.demo.Specification;

import com.example.demo.model.Customer;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CustomerSpecifications {

    public static Specification<Customer> hasSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String pattern = "%" + searchTerm.toLowerCase() + "%";

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("address")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("address2")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("district")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city").get("city")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city").get("country").get("country")), pattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
