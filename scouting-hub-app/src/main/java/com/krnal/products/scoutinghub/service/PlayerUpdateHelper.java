package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.Position;
import com.krnal.products.scoutinghub.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PlayerUpdateHelper implements UpdateHelper<Player, PlayerDTO> {

    private final FileStorageService fileStorageService;

    public PlayerUpdateHelper(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void set(Player player, PlayerDTO playerDTO) throws IOException {
        if (playerDTO.getTeam() != null && playerDTO.getTeam().getId() != null) {
            player.setTeam(new Team(playerDTO.getTeam().getId()));
        }
        if (playerDTO.getPosition() != null && playerDTO.getPosition().getId() != null) {
            player.setPosition(new Position(playerDTO.getPosition().getId()));
        }

        player.setUpdaterId(playerDTO.getUpdaterId()); // Not null based on your DTO definition
        player.setName(playerDTO.getName()); // Not null based on your DTO definition

        if (playerDTO.getImage() != null) {
            String imagePath = fileStorageService.storeFile(playerDTO.getImage());
            player.setImagePath(imagePath);
        }
        if (playerDTO.getNationality() != null) {
            player.setNationality(playerDTO.getNationality());
        }
        if (playerDTO.getCountryCode() != null){
            player.setCountryCode(playerDTO.getCountryCode());
        }
        if (playerDTO.getBirthDate() != null) {
            player.setBirthDate(playerDTO.getBirthDate());
        }
    }
}
