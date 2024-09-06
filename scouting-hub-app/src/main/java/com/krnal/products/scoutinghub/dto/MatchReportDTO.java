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
public class MatchReportDTO {
    public Integer id;
    public TeamDTO firstTeam;
    public TeamDTO secondTeam;
    public Integer firstTeamScore;
    public Integer secondTeamScore;
    @PastOrPresent
    public LocalDateTime createDate;
    @PastOrPresent
    public LocalDateTime updateDate;
    @NotNull
    public Date matchDate;
    @NotNull
    @NotBlank(message = "creatorId should not be null")
    public String creatorId;
    private Integer updaterId;
    public String season;
    public String generalNotes;
    public List<PlayerMatchReportDTO> firstTeamPlayersDTOList;
    public List<PlayerMatchReportDTO> secondTeamPlayersDTOList;
    public List<PlayerMatchReportDTO> playerMatchReportDTOList;

    public MatchReportDTO(Integer id) {
        this.id = id;
    }
}
