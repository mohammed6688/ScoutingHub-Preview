package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.FormationDTO;
import com.krnal.products.scoutinghub.dto.VideoReportDTO;
import com.krnal.products.scoutinghub.model.Formation;
import com.krnal.products.scoutinghub.model.VideoReport;
import org.mapstruct.Mapper;

@Mapper
public interface VideoReportMapper {
    VideoReport getVideoReport(VideoReportDTO videoReportDTO);
    VideoReportDTO getVideoReportDto(VideoReport videoReport);
}
