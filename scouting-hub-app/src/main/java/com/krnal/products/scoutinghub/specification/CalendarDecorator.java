package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class CalendarDecorator extends SpecificationDecorator<Calendar> {

    public CalendarDecorator(Specification<Calendar> spec) {
        super(spec);
    }

    @Override
    public Predicate toPredicate(Root<Calendar> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return super.toPredicate(root, query, builder);
    }
}
