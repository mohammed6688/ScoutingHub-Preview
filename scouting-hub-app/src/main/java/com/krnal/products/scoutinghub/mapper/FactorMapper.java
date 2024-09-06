package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.FactorDTO;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.model.Factor;
import com.krnal.products.scoutinghub.model.Position;
import org.mapstruct.Mapper;

@Mapper
public interface FactorMapper {
    Factor getFactor(FactorDTO factorDTO);
    FactorDTO getFactorDTO(Factor factor);
}
