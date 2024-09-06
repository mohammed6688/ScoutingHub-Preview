package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.FactorDTO;
import com.krnal.products.scoutinghub.dto.FormationDTO;
import com.krnal.products.scoutinghub.model.Factor;
import com.krnal.products.scoutinghub.model.Formation;
import org.mapstruct.Mapper;

@Mapper
public interface FormationMapper {
    Formation getFormation(FormationDTO formationDTO);
    FormationDTO getFormationDto(Formation factor);
}
