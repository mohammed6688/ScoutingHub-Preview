package com.krnal.products.scoutinghub.enums;

import lombok.Getter;

@Getter
public enum PlayerSuitabilityEnum {
    Suitable(1), NonSuitable(0);

    final Integer value;

    PlayerSuitabilityEnum(Integer value) {
        this.value = value;
    }
}