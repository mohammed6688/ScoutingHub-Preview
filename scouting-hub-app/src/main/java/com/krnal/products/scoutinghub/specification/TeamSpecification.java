package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.model.Zone;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TeamSpecification implements Specification<Team> {

    private SearchCriteria criteria;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TeamSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getKey().equalsIgnoreCase("zoneName")) {
            Join<Team, Zone> zoneJoin = root.join("zone", JoinType.INNER);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return builder.like(zoneJoin.get("zoneName"), criteria.getValue() + "%");
            } else {
                throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for zoneName");
            }
        }

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
                    return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
            default:
                throw new UnsupportedOperationException("Operation '" + criteria.getOperation() + "' not supported");
        }
    }
}


