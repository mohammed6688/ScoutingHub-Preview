package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.MatchReportDTO;
import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.dto.PlayerMatchReportDTO;
import com.krnal.products.scoutinghub.model.MatchReport;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.PlayerMatchReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {PositionMapper.class, PlayerMapper.class, TeamMapper.class, RatingMapper.class})
public interface PlayerMatchReportMapper {
    @Mapping(target = "matchReport", source = "matchReportDTO")
    @Mapping(target = "player", source = "playerDTO")
    @Mapping(target = "team", source = "teamDTO")
    @Mapping(target = "ratingsList", source = "ratingDTOList")
    @Mapping(target = "position", source = "positionDTO")
    PlayerMatchReport getPlayerMatchReport(PlayerMatchReportDTO playerMatchReportDTO);
    @Mapping(target = "matchReportDTO.playerMatchReportDTOList", ignore = true)
    @Mapping(target = "matchReportDTO", source = "matchReport")
    @Mapping(target = "playerDTO", source = "player")
    @Mapping(target = "teamDTO", source = "team")
    @Mapping(target = "ratingDTOList", source = "ratingsList")
    @Mapping(target = "positionDTO", source = "position")
    PlayerMatchReportDTO getPlayerMatchReportDTO(PlayerMatchReport playerMatchReport);


//    List<PlayerMatchReport> toEntityList(List<PlayerMatchReportDTO> dtoList);
//    List<PlayerMatchReportDTO> toDtoList(List<PlayerMatchReport> entityList);
}
