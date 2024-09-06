package com.krnal.products.scoutinghub.types;

import com.krnal.products.scoutinghub.dto.ShadowListDTO;

import java.util.List;

public record ShadowListResponse(List<ShadowListDTO> shadowLists, long totalElements) {}

