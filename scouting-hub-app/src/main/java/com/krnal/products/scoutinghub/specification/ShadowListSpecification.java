package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.*;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ShadowListSpecification implements Specification<ShadowList> {

    private SearchCriteria criteria;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ShadowListSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ShadowList> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // Handle LocalDateTime comparisons
        switch (criteria.getOperation()) {
            case ">":
                if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                    LocalDateTime dateTime = LocalDateTime.parse(criteria.getValue().toString(), DATE_TIME_FORMATTER);
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), dateTime);
                }
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case "<":
                if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                    LocalDateTime dateTime = LocalDateTime.parse(criteria.getValue().toString(), DATE_TIME_FORMATTER);
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), dateTime);
                }
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case ":":
                if (root.get(criteria.getKey()).getJavaType() == String.class) {
                    return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
        }
        return null;
    }
}
