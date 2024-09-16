package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.model.Zone;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class TeamDecorator extends SpecificationDecorator<Team> {

    private final SearchCriteria criteria;

    public TeamDecorator(Specification<Team> spec, SearchCriteria criteria) {
        super(spec);
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Team> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getKey().equalsIgnoreCase("zoneName")) {
            Join<Team, Zone> zoneJoin = root.join("zone", JoinType.INNER);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return builder.like(zoneJoin.get("zoneName"), "%" + criteria.getValue() + "%");
            } else {
                throw new UnsupportedOperationException("Operation " + criteria.getOperation() + " not supported for zoneName");
            }
        }
        return super.toPredicate(root, query, builder);
    }
}


