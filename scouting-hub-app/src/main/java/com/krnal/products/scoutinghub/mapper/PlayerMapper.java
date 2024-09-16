package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.utils.DateUtils;
import com.krnal.products.scoutinghub.utils.LogUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {PositionMapper.class, TeamMapper.class})
public interface PlayerMapper {
    Player getPlayer(PlayerDTO playerDTO);
    PlayerDTO getPlayerDTO(Player player);
    List<PlayerDTO> getPlayerDTOs(List<Player> players);

    @AfterMapping
    default void calculateAndSetAge(@MappingTarget PlayerDTO playerDTO, Player player) {
        if (player.getBirthDate() != null) {
            playerDTO.setAge(DateUtils.calculateAge(player.getBirthDate().toString()));
        }
    }
}
