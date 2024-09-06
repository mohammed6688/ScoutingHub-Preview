package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.ZoneDTO;
import com.krnal.products.scoutinghub.model.Zone;
import org.mapstruct.Mapper;

@Mapper
public interface ZoneMapper {
    Zone getZone(ZoneDTO zoneDTO);
    ZoneDTO getZoneDTO(Zone zone);
}
