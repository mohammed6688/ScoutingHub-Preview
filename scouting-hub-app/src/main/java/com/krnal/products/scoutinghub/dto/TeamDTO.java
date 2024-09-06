package com.krnal.products.scoutinghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO {
    public Integer id;
    public ZoneDTO zone;
    @PastOrPresent
    public LocalDateTime createDate;
    @PastOrPresent
    public LocalDateTime  updateDate;
    @NotNull
    @NotBlank(message = "creatorId should not be null")
    public String creatorId;
    @NotNull
    @NotBlank(message = "teamName should not be null")
    public String teamName;
    public MultipartFile logo;
    public String logoPath;
    public List<PlayerDTO> playerDTOList;
}
