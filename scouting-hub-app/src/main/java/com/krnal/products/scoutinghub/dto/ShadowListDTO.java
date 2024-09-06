package com.krnal.products.scoutinghub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShadowListDTO {
    public Integer id;
    public FormationDTO formation;
    @NotNull
    @NotBlank(message = "shadow list name should not be null")
    public String name;
    public String description;
    @PastOrPresent
    public LocalDateTime createDate;
    @PastOrPresent
    public LocalDateTime updateDate;
    @NotNull
    @NotBlank(message = "creatorId should not be null")
    public String creatorId;
    public Integer updaterId;

    public List<ShadowListPlayerDTO> shadowListPlayerList;

    public ShadowListDTO(Integer id) {
        this.id = id;
    }
}
