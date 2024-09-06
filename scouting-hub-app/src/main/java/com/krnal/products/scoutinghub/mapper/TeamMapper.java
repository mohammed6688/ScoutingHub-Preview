package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.TeamDTO;
import com.krnal.products.scoutinghub.dto.ZoneDTO;
import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.model.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ZoneMapper.class})
public interface TeamMapper {
    @Mapping(target = "zone", source = "zone")
    Team getTeam(TeamDTO teamDTO);
    @Mapping(source = "zone", target = "zone")
    TeamDTO getTeamDTO(Team team);
}
