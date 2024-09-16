package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dto.TeamDTO;
import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TeamUpdateHelper implements UpdateHelper<Team, TeamDTO>{

    private final FileStorageService fileStorageService;

    public TeamUpdateHelper(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void set(Team team, TeamDTO teamDTO) throws IOException {
        if (teamDTO.getZone() != null && teamDTO.getZone().getId() != null) {
            team.setZone(new Zone(teamDTO.getZone().getId()));
        }

        team.setTeamName(teamDTO.getTeamName()); // Not null based on your DTO definition

        if (teamDTO.getLogo() != null) {
            String imagePath = fileStorageService.storeFile(teamDTO.getLogo());
            team.setLogoPath(imagePath);
        }
    }
}
