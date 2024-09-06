package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.MatchReportDTO;
import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.model.MatchReport;
import com.krnal.products.scoutinghub.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TeamMapper.class, PlayerMatchReportMapper.class})
public interface MatchReportMapper {
    @Mapping(target = "firstTeam", source = "firstTeam")
    @Mapping(target = "secondTeam", source = "secondTeam")
    @Mapping(target = "playerMatchReportList", source = "playerMatchReportDTOList")
    MatchReport getMatchReport(MatchReportDTO matchReportDTO);
    @Mapping(target = "firstTeam", source = "firstTeam")
    @Mapping(target = "secondTeam", source = "secondTeam")
    @Mapping(target = "playerMatchReportDTOList", source = "playerMatchReportList")
    MatchReportDTO getMatchReportDTO(MatchReport matchReport);
}
