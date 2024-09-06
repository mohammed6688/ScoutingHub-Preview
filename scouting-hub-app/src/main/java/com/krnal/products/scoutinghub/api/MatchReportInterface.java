package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.dto.MatchReportDTO;
import com.krnal.products.scoutinghub.dto.PlayerDTO;
import com.krnal.products.scoutinghub.enums.OperationTypeEnum;
import com.krnal.products.scoutinghub.enums.ResourceTypeEnum;
import com.krnal.products.scoutinghub.events.CalendarEvent;
import com.krnal.products.scoutinghub.service.MatchReportService;
import com.krnal.products.scoutinghub.types.MatchReportResponse;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@RestController
@RequestMapping("/api")
public class MatchReportInterface {
    Logger logger = LoggerFactory.getLogger(MatchReportInterface.class);

    @Autowired
    MatchReportService matchReportService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @GetMapping("matchReports")
    public ResponseEntity<?> getAll() {
        String c = "MatchReportInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        MatchReportResponse matchReportResponse = matchReportService.getMatchReports();
        logger.info(createLogMessage(c, m, "Success", "playersSize", String.valueOf(matchReportResponse.matchReports().size())));
        return ResponseEntity.status(HttpStatus.OK).body(matchReportResponse);
    }

    @GetMapping("matchReport/{id}")
    public ResponseEntity<?> getMatchReport(@PathVariable("id") Integer id) {
        String c = "MatchReportInterface";
        String m = "getMatchReport";
        logger.info(createLogMessage(c, m, "Start"));
        MatchReportDTO matchReportDTO = matchReportService.getMatchReport(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(matchReportDTO);
    }

    @PostMapping("matchReport/search")
    public ResponseEntity<?> searchMatchReport(
            @RequestBody List<SearchCriteria> searchCriteriaList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        String c = "MatchReportInterface";
        String m = "searchMatchReport";
        logger.info(createLogMessage(c, m, "Start"));
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        MatchReportResponse matchReports = matchReportService.searchMatchReports(searchCriteriaList, pageRequest);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(matchReports);
    }

    @PostMapping("matchReport")
    public ResponseEntity<?> addMatchReport(@Valid @RequestBody MatchReportDTO matchReportDTO) {
        String c = "MatchReportInterface";
        String m = "addMatchReport";
        logger.info(createLogMessage(c, m, "Start"));
        MatchReportDTO matchReport = matchReportService.addMatchReport(matchReportDTO);
        eventPublisher.publishEvent(new CalendarEvent(matchReport.getCreatorId(),
                OperationTypeEnum.Add.getId(), ResourceTypeEnum.MatchReport.getId(), matchReport.getId()));
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.CREATED).body(matchReport);
    }

    @PutMapping("matchReport")
    public ResponseEntity<?> updateMatchReport(@Valid @RequestBody MatchReportDTO matchReportDTO) {
        String c = "MatchReportInterface";
        String m = "updateMatchReport";
        logger.info(createLogMessage(c, m, "Start"));
        MatchReportDTO matchReport = matchReportService.updateMatchReport(matchReportDTO);
        eventPublisher.publishEvent(new CalendarEvent(matchReport.getCreatorId(),
                OperationTypeEnum.Update.getId(), ResourceTypeEnum.MatchReport.getId(), matchReport.getId()));
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(matchReport);
    }

    @DeleteMapping("matchReport")
    public ResponseEntity<?> deleteMatchReport(@RequestParam("id") Integer id) {
        String c = "MatchReportInterface";
        String m = "deleteMatchReport";
        logger.info(createLogMessage(c, m, "Start"));
        matchReportService.deleteMatchReport(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
