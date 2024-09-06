package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.TeamRepo;
import com.krnal.products.scoutinghub.dto.TeamDTO;
import com.krnal.products.scoutinghub.mapper.PlayerMapper;
import com.krnal.products.scoutinghub.mapper.TeamMapper;
import com.krnal.products.scoutinghub.model.Player;
import com.krnal.products.scoutinghub.model.Team;
import com.krnal.products.scoutinghub.specification.TeamSpecification;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.types.TeamResponse;
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
public class TeamService {
    Logger logger = LoggerFactory.getLogger(TeamService.class);

    @Autowired
    TeamRepo teamRepo;
    @Autowired
    TeamMapper teamMapper;
    @Autowired
    TeamUpdateHelper teamUpdateHelper;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    PlayerMapper playerMapper;

    public TeamResponse getTeams() {
        String c = "TeamService";
        String m = "getTeams";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            List<Team> teams = teamRepo.findAll();
            List<TeamDTO> teamDTOList = teams.stream()
                    .map(team -> teamMapper.getTeamDTO(team))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new TeamResponse(teamDTOList, teams.size());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public TeamResponse getTeams(Pageable pageable) {
        String c = "TeamService";
        String m = "getTeams";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Page<Team> teams = teamRepo.findAll(pageable);
            List<TeamDTO> teamDTOList = teams.stream()
                    .map(team -> teamMapper.getTeamDTO(team))
                    .toList();
            logger.info(createLogMessage(c, m, "Success"));
            return new TeamResponse(teamDTOList, teams.getTotalElements());
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public TeamDTO getTeam(Integer id) {
        String c = "TeamService";
        String m = "getTeam";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Team> team = teamRepo.findById(id);
            if (team.isPresent()){
                TeamDTO teamDTO = teamMapper.getTeamDTO(team.get());
                Optional <List<Player>> optionalPlayers = teamRepo.findPlayersByTeamId(teamDTO.getId());
                if (optionalPlayers.isPresent()){
                    teamDTO.setPlayerDTOList(optionalPlayers.get().stream().map(player -> playerMapper.getPlayerDTO(player)).collect(Collectors.toList()));
                }
                logger.info(createLogMessage(c, m, "Success"));
                return teamDTO;
            }
            throw new RuntimeException(TEAM_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public Resource getTeamLogo(String filePath) {
        String c = "TeamService";
        String m = "getTeamLogo";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Resource resource = fileStorageService.loadFileAsResource(filePath);
            if (resource != null && resource.exists()) {
                return resource;
            }
            throw new RuntimeException(TEAM_IMAGE_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public TeamDTO addTeam(TeamDTO teamDTO) {
        String c = "TeamService";
        String m = "addTeam";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Team> teamOptional = teamRepo.findByTeamName(teamDTO.getTeamName());
            if (teamOptional.isEmpty()) { // if provided team name is not exist
                Team team = teamMapper.getTeam(teamDTO);
                if(teamDTO.getLogo() !=null&& !teamDTO.getLogo().isEmpty()) {
                    String imagePath = fileStorageService.storeFile(teamDTO.getLogo());
                    team.setLogoPath(imagePath);
                }
                team = teamRepo.save(team);
                logger.info(createLogMessage(c, m, "Success"));
                return teamMapper.getTeamDTO(team);
            }
            throw new RuntimeException(TEAM_NAME_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public TeamDTO updateTeam(TeamDTO teamDTO) {
        String c = "TeamService";
        String m = "updateTeam";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Team> teamOptional = teamRepo.findById(teamDTO.getId());
            if (teamOptional.isPresent()) { // if provided team id is existed
                Team team = teamOptional.get();
                teamUpdateHelper.set(team, teamDTO);
                team = teamRepo.save(team);
                logger.info(createLogMessage(c, m, "Success"));
                return teamMapper.getTeamDTO(team);
            }
            throw new RuntimeException(TEAM_ID_NOT_EXIST);
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public void deleteTeam(Integer id) {
        String c = "TeamService";
        String m = "deleteTeam";
        try {
            logger.info(createLogMessage(c, m, "Start"));
            Optional<Team> teamOptional = teamRepo.findById(id);
            if (teamOptional.isPresent()) { // if provided team id is existed
                teamRepo.deleteById(id);
                logger.info(createLogMessage(c, m, "Success"));
                return;
            }
            throw new RuntimeException(TEAM_ID_NOT_EXIST);
        } catch (DataIntegrityViolationException e){
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(TEAM_IS_REFERENCED, e); // Re-throw as unchecked exception
        }catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public TeamResponse searchTeams(List<SearchCriteria> params, Pageable pageable) {
        String c = "TeamService";
        String m = "searchTeams";

        try {
            logger.info(createLogMessage(c, m, "Start"));

            if (params == null || params.isEmpty()) {
                return getTeams(pageable);
            }
            Specification<Team> spec = Specification.where(createSpecification(params.getFirst()));

            for (int i = 1; i < params.size(); i++) {
                spec = spec.and(createSpecification(params.get(i)));
            }

            Page<Team> teams = teamRepo.findAll(spec, pageable);
            List<TeamDTO> teamDTOList = teams.stream()
                    .map(team -> teamMapper.getTeamDTO(team))
                    .toList();

            return new TeamResponse(teamDTOList, teams.getTotalElements());

        } catch (Exception e){
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private Specification<Team> createSpecification(SearchCriteria criteria) {
        return new TeamSpecification(criteria);
    }
}