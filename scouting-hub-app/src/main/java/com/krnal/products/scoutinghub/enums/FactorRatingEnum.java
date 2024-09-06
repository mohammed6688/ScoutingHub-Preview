package com.krnal.products.scoutinghub.enums;

import lombok.Getter;

@Getter
public enum FactorRatingEnum {
    Skilled(1), Average(0), Deficit(-1);

    final Integer ratingValue;

    FactorRatingEnum(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
}