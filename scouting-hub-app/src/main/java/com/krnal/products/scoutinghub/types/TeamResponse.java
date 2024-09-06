package com.krnal.products.scoutinghub.types;

import com.krnal.products.scoutinghub.dto.TeamDTO;

import java.util.List;

public record TeamResponse(List<TeamDTO> teams, long totalElements) {}
