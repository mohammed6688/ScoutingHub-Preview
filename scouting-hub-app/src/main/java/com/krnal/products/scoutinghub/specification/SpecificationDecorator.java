package com.krnal.products.scoutinghub.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public abstract class SpecificationDecorator<T> implements Specification<T> {
    protected final Specification<T> spec;

    public SpecificationDecorator(Specification<T> spec) {
        this.spec = spec;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return spec.toPredicate(root, query, criteriaBuilder);
    }
}
