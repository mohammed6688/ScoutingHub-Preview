package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class ShadowListDecorator extends SpecificationDecorator<ShadowList> {

    public ShadowListDecorator(Specification<ShadowList> spec) {
        super(spec);
    }

    @Override
    public Predicate toPredicate(Root<ShadowList> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return super.toPredicate(root, query, builder);
    }
}
