package com.krnal.products.scoutinghub.types;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FactorEfficiency {
    String factorName;
    Integer factorRating;
    Integer reportsCount;
    Integer skilledCount;
    Integer averageCount;
    Integer deficitCount;
}
