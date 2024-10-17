package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.authorization.RequiresRoles;
import com.krnal.products.scoutinghub.configs.Configs;
import com.krnal.products.scoutinghub.dto.TeamDTO;
import com.krnal.products.scoutinghub.service.TeamService;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.types.TeamResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@RestController
@RequestMapping("/api")
public class TeamInterface {
    Logger logger = LoggerFactory.getLogger(TeamInterface.class);

    @Autowired
    TeamService teamService;

    @GetMapping("teams")
    public ResponseEntity<?> getAll() {
        String c = "TeamInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        TeamResponse teamResponse = teamService.getTeams();
        logger.info(createLogMessage(c, m, "Success", "teamsSize", String.valueOf(teamResponse.teams().size())));
        return ResponseEntity.status(HttpStatus.OK).body(teamResponse);
    }

    @GetMapping("team/{id}")
    public ResponseEntity<?> getTeam(@PathVariable("id") Integer id) {
        String c = "TeamInterface";
        String m = "getTeam";
        logger.info(createLogMessage(c, m, "Start"));
        TeamDTO teamDTO = teamService.getTeam(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(teamDTO);
    }

    @GetMapping("team/image/download/{fileName}")
    public ResponseEntity<?> getTeamLogo(@PathVariable("fileName") String fileName) {
        String c = "TeamInterface";
        String m = "getTeamLogo";
        logger.info(createLogMessage(c, m, "Start"));
        Resource resource = teamService.getTeamLogo(fileName);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("team/search")
    public ResponseEntity<?> searchTeam(
            @RequestBody List<SearchCriteria> searchCriteriaList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        String c = "TeamInterface";
        String m = "searchTeam";
        logger.info(createLogMessage(c, m, "Start"));
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        TeamResponse teams = teamService.searchTeams(searchCriteriaList, pageRequest);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(teams);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PostMapping("team")
    public ResponseEntity<?> addTeam(@Valid TeamDTO teamDTO) {
        String c = "TeamInterface";
        String m = "addTeam";
        logger.info(createLogMessage(c, m, "Start"));
        TeamDTO creatTeam = teamService.addTeam(teamDTO);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.CREATED).body(creatTeam);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PutMapping("team")
    public ResponseEntity<?> updateTeam(@Valid TeamDTO zoneDTO) {
        String c = "TeamInterface";
        String m = "updateTeam";
        logger.info(createLogMessage(c, m, "Start"));
        TeamDTO updateTeam = teamService.updateTeam(zoneDTO);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(updateTeam);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @DeleteMapping("team")
    public ResponseEntity<?> deleteTeam(@RequestParam("id") Integer id) {
        String c = "TeamInterface";
        String m = "deleteTeam";
        logger.info(createLogMessage(c, m, "Start"));
        teamService.deleteTeam(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
