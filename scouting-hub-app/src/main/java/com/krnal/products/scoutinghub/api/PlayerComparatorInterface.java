package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.service.PlayerService;
import com.krnal.products.scoutinghub.service.StatisticsService;
import com.krnal.products.scoutinghub.types.FactorEfficiency;
import com.krnal.products.scoutinghub.types.PlayerComparatorResponse;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@RestController
@RequestMapping("/api")
public class PlayerComparatorInterface {
    Logger logger = LoggerFactory.getLogger(PlayerComparatorInterface.class);

    @Autowired
    StatisticsService statisticsService;
    @Autowired
    PlayerService playerService;

    @GetMapping("playerComparator")
    public ResponseEntity<?> getAll() {
        String c = "PlayerComparatorInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        PlayerComparatorResponse playerComparatorResponse = statisticsService.getAllPlayersFactorsEfficiency();
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(playerComparatorResponse);
    }


    @GetMapping("playerComparator/{id}")
    public ResponseEntity<?> getPlayerComparator(@PathVariable("id") Integer id) {
        String c = "PlayerComparatorInterface";
        String m = "getPlayerComparator";
        logger.info(createLogMessage(c, m, "Start"));
        PlayerComparatorResponse playerComparatorResponse = statisticsService.getPlayersFactorsEfficiency(new ArrayList<>(List.of(playerService.getPlayer(id))));
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(playerComparatorResponse);
    }


    @PostMapping("playerComparator/search")
    public ResponseEntity<?> searchPlayerComparator(
            @RequestBody List<SearchCriteria> searchCriteriaList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        String c = "PlayerComparatorInterface";
        String m = "searchPlayerComparator";
        logger.info(createLogMessage(c, m, "Start"));
        List<PlayerDTO> playerDTOList = playerService.searchPlayers(searchCriteriaList, PageRequest.of(page, size)).players();
        PlayerComparatorResponse playerComparatorResponse = statisticsService.getPlayersFactorsEfficiency(playerDTOList);
        playerComparatorResponse = statisticsService.sortPayload(playerComparatorResponse, sortDir, sortBy);

        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(playerComparatorResponse);
    }

}
