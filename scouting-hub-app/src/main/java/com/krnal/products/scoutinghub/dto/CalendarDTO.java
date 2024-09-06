package com.krnal.products.scoutinghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarDTO {
    private Integer id;
    @NotNull
    @NotBlank(message = "creator name cannot be null")
    private String creator;
    @NotNull
    @NotBlank(message = "operation type cannot be null")
    private Integer operationType;
    @NotNull
    @NotBlank(message = "resource cannot be null")
    private Integer resourceType;
    @NotNull
    @NotBlank(message = "resource id cannot be null")
    private Integer resourceId;
    private LocalDateTime createDate;

    public CalendarDTO(String creator, Integer operationType, Integer resourceType, Integer resourceId) {
        this.creator = creator;
        this.operationType = operationType;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
}
