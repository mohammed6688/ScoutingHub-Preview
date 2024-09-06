package com.krnal.products.scoutinghub.types;

import com.krnal.products.scoutinghub.dto.ZoneDTO;

import java.util.List;

public record ZoneResponse(List<ZoneDTO> zones, long totalElements) {}
