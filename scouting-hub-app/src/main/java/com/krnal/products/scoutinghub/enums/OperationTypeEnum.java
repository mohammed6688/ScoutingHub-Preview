package com.krnal.products.scoutinghub.enums;

import lombok.Getter;

@Getter
public enum OperationTypeEnum {
    Add(1), Update(2);

    final Integer id;

    OperationTypeEnum(Integer id) {
        this.id = id;
    }
}
