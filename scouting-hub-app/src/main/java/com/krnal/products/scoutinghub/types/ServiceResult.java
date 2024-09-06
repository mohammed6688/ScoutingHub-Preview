package com.krnal.products.scoutinghub.types;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ServiceResult {
    private int status;
    private String code;
    private String message;
    private Object object;

    public ServiceResult(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
