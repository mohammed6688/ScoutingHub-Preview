package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.Zone;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class ZoneDecorator extends SpecificationDecorator<Zone> {

    public ZoneDecorator(Specification<Zone> spec) {
        super(spec);
    }

    @Override
    public Predicate toPredicate(Root<Zone> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return super.toPredicate(root, query, builder);
    }
}
