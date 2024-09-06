package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.dto.RatingDTO;
import com.krnal.products.scoutinghub.model.Position;
import com.krnal.products.scoutinghub.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {FactorMapper.class})
public interface RatingMapper {
    Rating getRating(RatingDTO ratingDTO);
    @Mapping(target = "playerMatchReport", ignore = true)
    RatingDTO getRatingDTO(Rating rating);
}
