package com.krnal.products.scoutinghub.types;

import com.krnal.products.scoutinghub.dto.MatchReportDTO;

import java.util.List;

public record MatchReportResponse(List<MatchReportDTO> matchReports, long totalElements) {}
