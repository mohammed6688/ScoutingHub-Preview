package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.model.Zone;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ZoneSpecification implements Specification<Zone> {
    private final SearchCriteria criteria;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ZoneSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Zone> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case ">":
                return buildComparisonPredicate(root, builder, criteria.getKey(), criteria.getValue(), true);
            case "<":
                return buildComparisonPredicate(root, builder, criteria.getKey(), criteria.getValue(), false);
            case ">=":
                return buildComparisonPredicate(root, builder, criteria.getKey(), criteria.getValue(), true, true);
            case "<=":
                return buildComparisonPredicate(root, builder, criteria.getKey(), criteria.getValue(), false, true);
            case "!=":
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case "IS NULL":
                return builder.isNull(root.get(criteria.getKey()));
            case ":":
                if (root.get(criteria.getKey()).getJavaType() == String.class) {
                    return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
            default:
                throw new UnsupportedOperationException("Operation '" + criteria.getOperation() + "' not supported");
        }
    }


    private Predicate buildComparisonPredicate(Root<Zone> root, CriteriaBuilder builder, String key, Object value, boolean isGreaterThan) {
        return buildComparisonPredicate(root, builder, key, value, isGreaterThan, false);
    }

    private Predicate buildComparisonPredicate(Root<Zone> root, CriteriaBuilder builder, String key, Object value, boolean isGreaterThan, boolean includeEqual) {
        if (root.get(key).getJavaType() == LocalDateTime.class) {
            LocalDateTime dateTimeValue = LocalDateTime.parse(value.toString(), DATE_TIME_FORMATTER);
            if (isGreaterThan) {
                return includeEqual ? builder.greaterThanOrEqualTo(root.get(key), dateTimeValue) : builder.greaterThan(root.get(key), dateTimeValue);
            } else {
                return includeEqual ? builder.lessThanOrEqualTo(root.get(key), dateTimeValue) : builder.lessThan(root.get(key), dateTimeValue);
            }
        } else {

            if (isGreaterThan) {
                return includeEqual ? builder.greaterThanOrEqualTo(root.get(key), value.toString()) : builder.greaterThan(root.get(key), value.toString());
            } else {
                return includeEqual ? builder.lessThanOrEqualTo(root.get(key), value.toString()) : builder.lessThan(root.get(key), value.toString());
            }
        }
    }
}
