package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dto.ShadowListDTO;
import com.krnal.products.scoutinghub.dto.ShadowListPlayerDTO;
import com.krnal.products.scoutinghub.mapper.ShadowListPlayerMapper;
import com.krnal.products.scoutinghub.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.krnal.products.scoutinghub.constants.Constant.PLAYER_SHADOW_LIST_ID_NOT_EXIST;

@Component
public class ShadowListUpdateHelper implements UpdateHelper<ShadowList, ShadowListDTO> {

    private final ShadowListPlayerMapper shadowlistplayerMapper;

    public ShadowListUpdateHelper(ShadowListPlayerMapper shadowlistplayerMapper) {
        this.shadowlistplayerMapper = shadowlistplayerMapper;
    }

    @Override
    public void set(ShadowList shadowList, ShadowListDTO shadowListDTO) {
        if (shadowListDTO.getFormation() != null) {
            shadowList.setFormation(shadowList.getFormation());
        }

        if (shadowListDTO.getName() != null) {
            shadowList.setName(shadowList.getName());
        }

        if (shadowListDTO.getDescription() != null) {
            shadowList.setDescription(shadowList.getDescription());
        }

        if (shadowListDTO.getUpdaterId() != null) {
            shadowList.setUpdaterId(shadowList.getUpdaterId());
        }

        if (shadowListDTO.getShadowListPlayerList() != null && !shadowListDTO.getShadowListPlayerList().isEmpty()) {
            deleteMissingEntities(shadowList.getShadowListPlayerList(), shadowListDTO.getShadowListPlayerList().stream().map(ShadowListPlayerDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet()));
            setShadowListPlayer(shadowList.getShadowListPlayerList(), shadowListDTO.getShadowListPlayerList(), shadowList);
        }
    }


    private void setShadowListPlayer(List<ShadowListPlayer> shadowListPlayerList, List<ShadowListPlayerDTO> shadowListPlayerDTOList, ShadowList shadowList) {
        for (ShadowListPlayerDTO shadowListPlayerDTO : shadowListPlayerDTOList) {

            if (shadowListPlayerDTO.getId() != null) { // add operation
                Optional<ShadowListPlayer> shadowListPlayerOptional = shadowListPlayerList.stream()
                        .filter(shadowListPlayer -> shadowListPlayer.getId().equals(shadowListPlayerDTO.getId()))
                        .findFirst();

                // update the shadow list player
                if (shadowListPlayerOptional.isEmpty()) {
                    throw new IllegalArgumentException(PLAYER_SHADOW_LIST_ID_NOT_EXIST);
                }
                updateShadowListPlayer(shadowListPlayerOptional.get(), shadowListPlayerDTO);

            } else {  // update operation
                saveShadowListPlayer(shadowListPlayerDTO, shadowList);
            }
        }
    }

    private void updateShadowListPlayer(ShadowListPlayer shadowListPlayer, ShadowListPlayerDTO shadowListPlayerDTO) {
        if (shadowListPlayerDTO.getPlayer() != null) {
            shadowListPlayer.setPlayer(new Player(shadowListPlayerDTO.getPlayer().getId()));
        }

        if (shadowListPlayerDTO.getPosition() != null) {
            shadowListPlayer.setPosition(new Position(shadowListPlayerDTO.getPosition().getId()));
        }
    }

    public void saveShadowListPlayer(ShadowListPlayerDTO shadowListPlayerDTO, ShadowList shadowList) {
        shadowListPlayerDTO.setShadowList(new ShadowListDTO(shadowList.getId()));
        ShadowListPlayer shadowListPlayer = shadowlistplayerMapper.getShadowListPlayer(shadowListPlayerDTO);
        List<ShadowListPlayer> shadowListPlayerList = shadowList.getShadowListPlayerList();

        if (shadowListPlayerList != null) {
            shadowList.getShadowListPlayerList().add(shadowListPlayer);
        } else {
            List<ShadowListPlayer> shadowListPlayers = new ArrayList<>();
            shadowListPlayers.add(shadowListPlayer);
            shadowList.setShadowListPlayerList(shadowListPlayers);
        }
    }

    private <T> void deleteMissingEntities(List<T> entityList, Set<Integer> dtoIds) {
        List<T> entitiesToDelete = entityList.stream()
                .filter(entity -> !dtoIds.contains(getEntityId(entity)))
                .toList();

        entityList.removeAll(entitiesToDelete);
    }

    private Integer getEntityId(Object entity) {
        if (entity instanceof ShadowListPlayer) {
            return ((ShadowListPlayer) entity).getId();
        } else if (entity instanceof ShadowList) {
            return ((ShadowList) entity).getId();
        }
        return null;
    }

}
