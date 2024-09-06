package com.krnal.products.scoutinghub.types;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiError {
    private int status;
    private String message;
}
