package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.dto.ShadowListDTO;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.ShadowList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {FormationMapper.class, ShadowListPlayerMapper.class})
public interface ShadowListMapper {
    ShadowList getShadowList(ShadowListDTO shadowListDTO);
    @Mapping(target = "shadowListPlayerList.shadowList", ignore = true)
    ShadowListDTO getShadowListDto(ShadowList shadowList);
}
