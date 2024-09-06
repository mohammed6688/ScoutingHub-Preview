package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.dto.TeamDTO;
import com.krnal.products.scoutinghub.service.PlayerService;
import com.krnal.products.scoutinghub.types.PlayerResponse;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@RestController
@RequestMapping("/api")
public class PlayerInterface {
    Logger logger = LoggerFactory.getLogger(PlayerInterface.class);

    @Autowired
    PlayerService playerService;

    @GetMapping("players")
    public ResponseEntity<?> getAll() {
        String c = "PlayerInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        PlayerResponse playerResponse = playerService.getPlayers();
        logger.info(createLogMessage(c, m, "Success", "playersSize", String.valueOf(playerResponse.players().size())));
        return ResponseEntity.status(HttpStatus.OK).body(playerResponse);
    }

    @GetMapping("player/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable("id") Integer id) throws InterruptedException {
        String c = "PlayerInterface";
        String m = "getPlayer";
        logger.info(createLogMessage(c, m, "Start"));
        PlayerDTO playerDTO = playerService.getPlayer(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(playerDTO);
    }

    @GetMapping("player/image/download/{fileName}")
    public ResponseEntity<?> getPlayerImage(@PathVariable("fileName") String fileName) {
        String c = "PlayerInterface";
        String m = "getPlayerImage";
        logger.info(createLogMessage(c, m, "Start"));
        Resource resource = playerService.getPlayerImage(fileName);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("player/search")
    public ResponseEntity<?> searchPlayer(
            @RequestBody List<SearchCriteria> searchCriteriaList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        String c = "PlayerInterface";
        String m = "searchPlayer";
        logger.info(createLogMessage(c, m, "Start"));
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PlayerResponse players = playerService.searchPlayers(searchCriteriaList,pageRequest);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }

    @PostMapping(value = "player", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPlayer(@Valid PlayerDTO playerDTO) {
        String c = "PlayerInterface";
        String m = "addPlayer";
        logger.info(createLogMessage(c, m, "Start"));
        PlayerDTO player = playerService.addPlayer(playerDTO);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @PutMapping("player")
    public ResponseEntity<?> updatePlayer(@Valid PlayerDTO playerDTO) {
        String c = "PlayerInterface";
        String m = "updatePlayer";
        logger.info(createLogMessage(c, m, "Start"));
        PlayerDTO player = playerService.updatePlayer(playerDTO);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(player);
    }

    @DeleteMapping("player")
    public ResponseEntity<?> deletePlayer(@RequestParam("id") Integer id) {
        String c = "PlayerInterface";
        String m = "deletePlayer";
        logger.info(createLogMessage(c, m, "Start"));
        playerService.deletePlayer(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
