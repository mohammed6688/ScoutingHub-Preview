package com.krnal.products.scoutinghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZoneDTO {
    public Integer id;
//    @PastOrPresent
    public LocalDateTime createDate;
//    @PastOrPresent
    public LocalDateTime updateDate;
    @NotNull
    @NotBlank(message = "creatorId should not be null")
    public String creatorId;
    @NotNull
    @NotBlank(message = "zoneName should not be null")
    public String zoneName;
    public Byte[] logo;
}
