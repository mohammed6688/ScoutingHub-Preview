package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {FactorMapper.class})
public interface PositionMapper {
    Position getPosition(PositionDTO positionDTO);
    PositionDTO getPositionDTO(Position position);
}
