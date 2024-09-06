package com.krnal.products.scoutinghub.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private List<Object> values;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

}

