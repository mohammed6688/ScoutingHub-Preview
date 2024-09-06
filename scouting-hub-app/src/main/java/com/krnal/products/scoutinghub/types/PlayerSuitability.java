package com.krnal.products.scoutinghub.types;

import lombok.Data;

@Data
public class PlayerSuitability {
    public Integer suitability;
    public Integer reportsCount;
    public Integer suitableCount;
    public Integer nonSuitableCount;
}
