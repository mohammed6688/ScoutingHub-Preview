package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dto.ZoneDTO;
import com.krnal.products.scoutinghub.model.Zone;
import org.springframework.stereotype.Component;

@Component
public class ZoneUpdateHelper implements UpdateHelper<Zone, ZoneDTO> {

    @Override
    public void set(Zone zone, ZoneDTO zoneDTO) {
        zone.setZoneName(zoneDTO.getZoneName()); // Not null based on your DTO definition

        if (zoneDTO.getLogo() != null) {
            zone.setLogo(zoneDTO.getLogo());
        }
    }
}
