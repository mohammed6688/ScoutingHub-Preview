package com.krnal.products.scoutinghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShadowListPlayerDTO {
    public Integer id;
//    @NotNull
    public PlayerDTO player;
//    @NotNull
    public ShadowListDTO shadowList;
//    @NotNull
    public PositionDTO position;
}
