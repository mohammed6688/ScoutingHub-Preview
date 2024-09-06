package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.*;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchReportSpecification implements Specification<MatchReport> {

    private SearchCriteria criteria;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public MatchReportSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<MatchReport> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        // search by firstTeam name or secondTeam name or player name
        if (criteria.getKey().equalsIgnoreCase("name")) {
            Join<MatchReport, Team> firstTeamJoin = root.join("firstTeam", JoinType.INNER);
            Join<MatchReport, Team> secondTeamJoin = root.join("secondTeam", JoinType.INNER);
            Join<MatchReport, PlayerMatchReport> playerMatchReportJoin = root.join("playerMatchReportList", JoinType.INNER);
            Join<PlayerMatchReport, Player> playerJoin = playerMatchReportJoin.join("player", JoinType.INNER);

            if (criteria.getOperation().equalsIgnoreCase(":")) {
                Predicate firstTeamPredicate = builder.like(firstTeamJoin.get("teamName"), "%" + criteria.getValue() + "%");
                Predicate secondTeamPredicate = builder.like(secondTeamJoin.get("teamName"), "%" + criteria.getValue() + "%");
                Predicate playerNamePredicate = builder.like(playerJoin.get("name"), "%" + criteria.getValue() + "%");

                return builder.or(firstTeamPredicate, secondTeamPredicate, playerNamePredicate);
            } else {
                throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for teamName");
            }
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
