package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.authorization.RequiresRoles;
import com.krnal.products.scoutinghub.configs.Configs;
import com.krnal.products.scoutinghub.dto.ShadowListDTO;
import com.krnal.products.scoutinghub.enums.OperationTypeEnum;
import com.krnal.products.scoutinghub.enums.ResourceTypeEnum;
import com.krnal.products.scoutinghub.events.CalendarEvent;
import com.krnal.products.scoutinghub.service.ShadowListService;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.types.ShadowListResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@RestController
@RequestMapping("/api")
public class ShadowListInterface {
    Logger logger = LoggerFactory.getLogger(ShadowListInterface.class);

    @Autowired
    ShadowListService shadowListService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @GetMapping("shadowLists")
    public ResponseEntity<?> getAll() {
        String c = "ShadowListInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        ShadowListResponse shadowListResponse = shadowListService.getShadowLists();
        logger.info(createLogMessage(c, m, "Success", "shadowListsSize", String.valueOf(shadowListResponse.shadowLists().size())));
        return ResponseEntity.status(HttpStatus.OK).body(shadowListResponse);
    }

    @GetMapping("shadowList/{id}")
    public ResponseEntity<?> getShadowList(@PathVariable("id") Integer id) {
        String c = "ShadowListInterface";
        String m = "getShadowList";
        logger.info(createLogMessage(c, m, "Start"));
        ShadowListDTO shadowListDTO = shadowListService.getShadowList(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(shadowListDTO);
    }

    @PostMapping("shadowList/search")
    public ResponseEntity<?> searchShadowList(
            @RequestBody List<SearchCriteria> searchCriteriaList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        String c = "ShadowListInterface";
        String m = "searchShadowList";
        logger.info(createLogMessage(c, m, "Start"));
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        ShadowListResponse shadowLists = shadowListService.searchShadowLists(searchCriteriaList, pageRequest);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(shadowLists);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PostMapping(value = "shadowList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addShadowList(@Valid @RequestBody ShadowListDTO shadowListDTO) {
        String c = "ShadowListInterface";
        String m = "addShadowList";
        logger.info(createLogMessage(c, m, "Start"));
        ShadowListDTO shadowList = shadowListService.addShadowList(shadowListDTO);
        eventPublisher.publishEvent(new CalendarEvent(shadowList.getCreatorId(),
                OperationTypeEnum.Add.getId(), ResourceTypeEnum.ShadowList.getId(), shadowList.getId()));
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.CREATED).body(shadowList);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PutMapping("shadowList")
    public ResponseEntity<?> updateShadowList(@Valid @RequestBody ShadowListDTO shadowListDTO) {
        String c = "ShadowListInterface";
        String m = "updateShadowList";
        logger.info(createLogMessage(c, m, "Start"));
        ShadowListDTO shadowList = shadowListService.updateShadowList(shadowListDTO);
        eventPublisher.publishEvent(new CalendarEvent(shadowList.getCreatorId(),
                OperationTypeEnum.Update.getId(), ResourceTypeEnum.ShadowList.getId(), shadowList.getId()));
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(shadowList);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @DeleteMapping("shadowList")
    public ResponseEntity<?> deleteShadowList(@RequestParam("id") Integer id) {
        String c = "ShadowListInterface";
        String m = "deleteShadowList";
        logger.info(createLogMessage(c, m, "Start"));
        shadowListService.deleteShadowList(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
