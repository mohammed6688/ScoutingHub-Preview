package com.krnal.products.scoutinghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.krnal.products.scoutinghub.model.PlayerMatchReport;
import com.krnal.products.scoutinghub.types.FactorEfficiency;
import com.krnal.products.scoutinghub.types.PlayerSuitability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerDTO {
    public Integer id;
    public TeamDTO team;
    public PositionDTO position;
    @PastOrPresent
    public LocalDateTime createDate;
    @PastOrPresent
    public LocalDateTime updateDate;
    @NotNull
    @NotBlank(message = "creatorId should not be null")
    public String creatorId;
    @NotNull
    public Integer updaterId;
    @NotNull
    @NotBlank(message = "teamName should not be null")
    public String name;
    public MultipartFile image;
    public String imagePath;
    public String nationality;
    public String countryCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthDate;
    public String age;
    public PlayerSuitability playerSuitability;
    public List<FactorEfficiency> factorEfficiencieList;
    public List<PlayerMatchReportDTO> playerMatchReportList;
}
