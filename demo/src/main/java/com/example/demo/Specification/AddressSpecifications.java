package com.example.demo.Specification;

import com.example.demo.model.Address;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AddressSpecifications {

    public static Specification<Address> hasSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String pattern = "%" + searchTerm.toLowerCase() + "%";

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address2")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("district")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("postalCode")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city").get("city")), pattern));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city").get("country").get("country")), pattern));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
