package com.krnal.products.scoutinghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerMatchReportDTO {
//    @NotNull
    public Integer id;
//    @NotNull
    public MatchReportDTO matchReportDTO;
    @NotNull
    public PlayerDTO playerDTO;
    @NotNull
    public TeamDTO teamDTO;
    @NotNull
    public PositionDTO positionDTO;
    @NotNull
    public Integer finalRating;
    public String comments;
    @NotNull
    public List<RatingDTO> ratingDTOList;

    public PlayerMatchReportDTO(Integer id) {
        this.id = id;
    }
}
