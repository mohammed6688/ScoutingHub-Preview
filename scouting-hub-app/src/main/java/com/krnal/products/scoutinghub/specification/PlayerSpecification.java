package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.Position;
import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.model.Zone;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PlayerSpecification implements Specification<Player> {

    private SearchCriteria criteria;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public PlayerSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // Handle join and filter by zone name
        if (criteria.getKey().equalsIgnoreCase("zoneName")) {
            Join<Player, Team> teamJoin = root.join("team", JoinType.INNER);
            Join<Team, Zone> zoneJoin = teamJoin.join("zone", JoinType.INNER);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return builder.like(zoneJoin.get("zoneName"), criteria.getValue() + "%");
            } else {
                throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for zoneName");
            }
        }

        if (criteria.getKey().equalsIgnoreCase("teamName")) {
            Join<Player, Team> teamJoin = root.join("team", JoinType.INNER);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return builder.like(teamJoin.get("teamName"), criteria.getValue() + "%");
            } else {
                throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for teamName");
            }
        }

        if (criteria.getKey().equalsIgnoreCase("teamId")) {
            Join<Player, Team> teamJoin = root.join("team", JoinType.INNER);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return builder.equal(teamJoin.get("id"), criteria.getValue());
            } else {
                throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for teamName");
            }
        }

        // Handle join and filter by position name
        if (criteria.getKey().equalsIgnoreCase("positionName")) {
            Join<Player, Position> positionJoin = root.join("position", JoinType.INNER);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                if (criteria.getValues() != null) {
                    CriteriaBuilder.In<String> inClause = builder.in(positionJoin.get("positionName"));
                    for (Object value : criteria.getValues()) {
                        inClause.value(value.toString());
                    }
                    return inClause;
                } else {
                    return builder.like(positionJoin.get("positionName"), criteria.getValue() + "%");
                }
            } else {
                throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for positionName");
            }
        }

        if (criteria.getKey().equalsIgnoreCase("birthDate")) {
            long timestamp;
            try {
                timestamp = Long.parseLong(criteria.getValue().toString()); // Convert the timestamp to a long
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid timestamp format");
            }

            LocalDateTime dateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime(); // Convert timestamp to LocalDateTime
            LocalDate localDate = dateTime.toLocalDate(); // Extract LocalDate

            return switch (criteria.getOperation()) {
                case ">" -> builder.greaterThanOrEqualTo(root.get(criteria.getKey()), java.sql.Date.valueOf(localDate));
                case "<" -> builder.lessThanOrEqualTo(root.get(criteria.getKey()), java.sql.Date.valueOf(localDate));
                case ":" -> builder.equal(root.get(criteria.getKey()), java.sql.Date.valueOf(localDate));
                default ->
                        throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for birthDate");
            };
        }

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
                    return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
                } else {
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
        }
        return null;
    }
}
