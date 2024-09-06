package com.krnal.products.scoutinghub.types;

import com.krnal.products.scoutinghub.dto.PlayerDTO;

import java.util.List;

public record PlayerResponse(List<PlayerDTO> players, long totalElements) {}

