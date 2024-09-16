package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.*;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class MatchReportDecorator extends SpecificationDecorator<MatchReport> {

    private final SearchCriteria criteria;

    public MatchReportDecorator(Specification<MatchReport> spec, SearchCriteria criteria) {
        super(spec);
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

        return super.toPredicate(root, query, builder);
    }
}
