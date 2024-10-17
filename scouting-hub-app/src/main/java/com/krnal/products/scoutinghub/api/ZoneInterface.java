package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.authorization.RequiresRoles;
import com.krnal.products.scoutinghub.configs.Configs;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.dto.ZoneDTO;
import com.krnal.products.scoutinghub.service.ZoneService;
import com.krnal.products.scoutinghub.types.ZoneResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@RestController
@RequestMapping("/api")
public class ZoneInterface {
    Logger logger = LoggerFactory.getLogger(ZoneInterface.class);

    @Autowired
    ZoneService zoneService;

    @GetMapping("zones")
    public ResponseEntity<?> getAll() {
        String c = "ZoneInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        ZoneResponse zones = zoneService.getZones();
        logger.info(createLogMessage(c, m, "Success", "zonesSize", String.valueOf(zones.zones().size())));
        return ResponseEntity.status(HttpStatus.OK).body(zones);
    }

    @GetMapping("zone/{id}")
    public ResponseEntity<?> getZone(@PathVariable("id") Integer id) {
        String c = "ZoneInterface";
        String m = "getZone";
        logger.info(createLogMessage(c, m, "Start"));
        ZoneDTO zoneDTO = zoneService.getZone(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(zoneDTO);
    }

    @PostMapping("zone/search")
    public ResponseEntity<?> searchZone(
            @RequestBody List<SearchCriteria> searchCriteriaList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        String c = "ZoneInterface";
        String m = "searchZone";
        logger.info(createLogMessage(c, m, "Start"));
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        ZoneResponse zones = zoneService.searchZones(searchCriteriaList, pageRequest);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(zones);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PostMapping("zone")
    public ResponseEntity<?> addZone(@Valid @RequestBody ZoneDTO zoneDTO) {
        String c = "ZoneInterface";
        String m = "addZone";
        logger.info(createLogMessage(c, m, "Start"));
        ZoneDTO createdZone = zoneService.addZone(zoneDTO);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdZone);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PutMapping("zone")
    public ResponseEntity<?> updateZone(@Valid @RequestBody ZoneDTO zoneDTO) {
        String c = "ZoneInterface";
        String m = "updateZone";
        logger.info(createLogMessage(c, m, "Start"));
        ZoneDTO updatedZone = zoneService.updateZone(zoneDTO);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(updatedZone);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @DeleteMapping("zone")
    public ResponseEntity<?> deleteZone(@RequestParam("id") Integer id) {
        String c = "ZoneInterface";
        String m = "updateZone";
        logger.info(createLogMessage(c, m, "Start"));
        zoneService.deleteZone(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
