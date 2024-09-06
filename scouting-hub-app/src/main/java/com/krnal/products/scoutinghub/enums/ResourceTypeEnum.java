package com.krnal.products.scoutinghub.enums;

import lombok.Getter;

@Getter
public enum ResourceTypeEnum {
    MatchReport(1), VideoReport(2), ShadowList(3);

    final Integer id;

    ResourceTypeEnum(Integer id) {
        this.id = id;
    }
}
