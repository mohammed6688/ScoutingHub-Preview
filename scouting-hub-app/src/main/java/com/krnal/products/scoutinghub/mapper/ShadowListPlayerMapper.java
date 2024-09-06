package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.FormationDTO;
import com.krnal.products.scoutinghub.dto.ShadowListPlayerDTO;
import com.krnal.products.scoutinghub.model.Formation;
import com.krnal.products.scoutinghub.model.ShadowListPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ShadowListPlayerMapper {
    ShadowListPlayer getShadowListPlayer(ShadowListPlayerDTO shadowListPlayerDTO);
    @Mapping(target = "shadowList", ignore = true)
    ShadowListPlayerDTO getShadowListPlayerDto(ShadowListPlayer shadowListPlayer);
}
