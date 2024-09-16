package com.krnal.products.scoutinghub.specification;

import com.krnal.products.scoutinghub.model.VideoReport;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class VideoReportDecorator extends SpecificationDecorator<VideoReport> {

    public VideoReportDecorator(Specification<VideoReport> spec) {
        super(spec);
    }

    @Override
    public Predicate toPredicate(Root<VideoReport> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return super.toPredicate(root, query, builder);
    }
}
