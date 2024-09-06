package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.PlayerRepo;
import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.mapper.PlayerMapper;
import com.krnal.products.scoutinghub.mapper.PlayerMatchReportMapper;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.PlayerMatchReport;
import com.krnal.products.scoutinghub.specification.PlayerSpecification;
import com.krnal.products.scoutinghub.types.PlayerResponse;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.krnal.products.scoutinghub.constants.Constant.*;
import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Service
public class PlayerService {
    Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    PlayerRepo playerRepo;
    @Autowired
    PlayerMapper playerMapper;
    @Autowired
    PlayerUpdateHelper playerUpdateHelper;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    PlayerMatchReportMapper playerMatchReportMapper;
    @Autowired
    StatisticsService statisticsService;

    public PlayerResponse getPlayers() {
        String c = "PlayerService";
        String m = "getPlayers";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<Player> playerList = playerRepo.findAll();
            List<PlayerDTO> playerDTOList = playerList.stream()
                    .map(player -> playerMapper.getPlayerDTO(player))
                    .collect(Collectors.toList());
            logger.info(createLogMessage(c, m, "Success"));
            return new PlayerResponse(playerDTOList, playerList.size());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public PlayerResponse getPlayers(Pageable pageable) {
        String c = "PlayerService";
        String m = "getPlayers";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Page<Player> playerList = playerRepo.findAll(pageable);
            List<PlayerDTO> playerDTOList = playerList.stream()
                    .map(player -> playerMapper.getPlayerDTO(player))
                    .collect(Collectors.toList());
            logger.info(createLogMessage(c, m, "Success"));
            return new PlayerResponse(playerDTOList, playerList.getTotalElements());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public PlayerDTO getPlayer(Integer id) {
        String c = "PlayerService";
        String m = "getPlayer";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Player> player = playerRepo.findById(id);
            if (player.isPresent()) {
                PlayerDTO playerDTO = playerMapper.getPlayerDTO(player.get());
                Optional<List<PlayerMatchReport>> optionalPlayerMatchReports = playerRepo.findPlayerMatchReportByPlayerId(playerDTO.getId());

                if (optionalPlayerMatchReports.isPresent()) {
                    playerDTO.setPlayerMatchReportList(optionalPlayerMatchReports.get().stream().map(playerMatchReport -> playerMatchReportMapper.getPlayerMatchReportDTO(playerMatchReport)).collect(Collectors.toList()));
                }

                playerDTO.setFactorEfficiencieList(statisticsService.getFactorsEfficiency(id));
                playerDTO.setPlayerSuitability(statisticsService.getPlayerSuitability(id));

                logger.info(createLogMessage(c, m, "Success"));
                return playerDTO;
            }
            throw new RuntimeException(PLAYER_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public Resource getPlayerImage(String filePath) {
        String c = "PlayerService";
        String m = "getPlayerImage";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Resource resource = fileStorageService.loadFileAsResource(filePath);
            if (resource != null && resource.exists()) {
                return resource;
            }
            throw new RuntimeException(PLAYER_IMAGE_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public PlayerDTO addPlayer(PlayerDTO playerDTO) {
        String c = "PlayerService";
        String m = "addPlayer";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Player> playerOptional = playerRepo.findByName(playerDTO.getName());
            if (playerOptional.isEmpty()) { // if provided player is not exist
                Player player = playerMapper.getPlayer(playerDTO);
                if (playerDTO.getImage() != null && !playerDTO.getImage().isEmpty()) {
                    String imagePath = fileStorageService.storeFile(playerDTO.getImage());
                    player.setImagePath(imagePath);
                }
                player = playerRepo.save(player);
                logger.info(createLogMessage(c, m, "Success"));
                return playerMapper.getPlayerDTO(player);
            }
            throw new RuntimeException(PLAYER_NAME_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public PlayerDTO updatePlayer(PlayerDTO playerDTO) {
        String c = "PlayerService";
        String m = "updatePlayer";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Player> playerOptional = playerRepo.findById(playerDTO.getId());
            if (playerOptional.isPresent()) { // if provided player id is existed
                Player player = playerOptional.get();
                playerUpdateHelper.set(player, playerDTO);
                player = playerRepo.save(player);
                logger.info(createLogMessage(c, m, "Success"));
                return playerMapper.getPlayerDTO(player);
            }
            throw new RuntimeException(PLAYER_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public void deletePlayer(Integer id) {
        String c = "PlayerService";
        String m = "deletePlayer";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Player> playerOptional = playerRepo.findById(id);
            if (playerOptional.isPresent()) { // if provided player id is existed
                playerRepo.deleteById(id);
                logger.info(createLogMessage(c, m, "Success"));
                return;
            }
            throw new RuntimeException(PLAYER_ID_NOT_EXIST);
        } catch (DataIntegrityViolationException e){
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(PLAYER_IS_REFERENCED, e); // Re-throw as unchecked exception
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public PlayerResponse searchPlayers(List<SearchCriteria> params, Pageable pageable) {
        String c = "PlayerService";
        String m = "searchPlayers";

        try {
            logger.info(createLogMessage(c, m, "Start"));

            if (params == null || params.isEmpty()) {
                return getPlayers(pageable);
            }
            Specification<Player> spec = Specification.where(createSpecification(params.getFirst()));

            for (int i = 1; i < params.size(); i++) {
                spec = spec.and(createSpecification(params.get(i)));
            }

            Page<Player> players = playerRepo.findAll(spec, pageable);
            List<PlayerDTO> playerDTOList = players.stream()
                    .map(player -> playerMapper.getPlayerDTO(player))
                    .toList();

            return new PlayerResponse(playerDTOList, players.getTotalElements());

        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private Specification<Player> createSpecification(SearchCriteria criteria) {
        return new PlayerSpecification(criteria);
    }
}