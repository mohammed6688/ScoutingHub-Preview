package com.krnal.products.scoutinghub.types;

import com.krnal.products.scoutinghub.dto.VideoReportDTO;

import java.util.List;

public record VideoReportResponse(List<VideoReportDTO> videoReports, long totalElements) {}
