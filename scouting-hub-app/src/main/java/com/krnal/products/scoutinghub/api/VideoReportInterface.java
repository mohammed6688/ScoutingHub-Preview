package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.authorization.RequiresRoles;
import com.krnal.products.scoutinghub.configs.Configs;
import com.krnal.products.scoutinghub.dto.VideoReportDTO;
import com.krnal.products.scoutinghub.enums.OperationTypeEnum;
import com.krnal.products.scoutinghub.enums.ResourceTypeEnum;
import com.krnal.products.scoutinghub.events.CalendarEvent;
import com.krnal.products.scoutinghub.service.VideoReportService;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import com.krnal.products.scoutinghub.types.VideoReportResponse;
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

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@RestController
@RequestMapping("/api")
public class VideoReportInterface {
    Logger logger = LoggerFactory.getLogger(VideoReportInterface.class);

    @Autowired
    VideoReportService videoReportService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @GetMapping("videoReports")
    public ResponseEntity<?> getAll() {
        String c = "VideoReportInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        VideoReportResponse videoReportResponse = videoReportService.getVideoReports();
        logger.info(createLogMessage(c, m, "Success", "zonesSize", String.valueOf(videoReportResponse.videoReports().size())));
        return ResponseEntity.status(HttpStatus.OK).body(videoReportResponse);
    }

    @GetMapping("videoReport/{id}")
    public ResponseEntity<?> getVideoReport(@PathVariable("id") Integer id) {
        String c = "VideoReportInterface";
        String m = "getVideoReport";
        logger.info(createLogMessage(c, m, "Start"));
        VideoReportDTO videoReportDTO = videoReportService.getVideoReport(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(videoReportDTO);
    }

    @PostMapping("videoReport/search")
    public ResponseEntity<?> searchVideoReport(
            @RequestBody List<SearchCriteria> searchCriteriaList,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        String c = "VideoReportInterface";
        String m = "searchVideoReport";
        logger.info(createLogMessage(c, m, "Start"));
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        VideoReportResponse videoReport = videoReportService.searchVideoReport(searchCriteriaList, pageRequest);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(videoReport);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PostMapping("videoReport")
    public ResponseEntity<?> addVideoReport(@Valid @RequestBody VideoReportDTO videoReportDTO) {
        String c = "VideoReportInterface";
        String m = "addVideoReport";
        logger.info(createLogMessage(c, m, "Start"));
        VideoReportDTO videoReport = videoReportService.addVideoReport(videoReportDTO);
        eventPublisher.publishEvent(new CalendarEvent(videoReport.getCreatorId(),
                OperationTypeEnum.Add.getId(), ResourceTypeEnum.VideoReport.getId(), videoReport.getId()));
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.CREATED).body(videoReport);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @PutMapping("videoReport")
    public ResponseEntity<?> updateVideoReport(@Valid @RequestBody VideoReportDTO videoReportDTO) {
        String c = "VideoReportInterface";
        String m = "updateVideoReport";
        logger.info(createLogMessage(c, m, "Start"));
        VideoReportDTO videoReport = videoReportService.updateVideoReport(videoReportDTO);
        eventPublisher.publishEvent(new CalendarEvent(videoReport.getCreatorId(),
                OperationTypeEnum.Update.getId(), ResourceTypeEnum.VideoReport.getId(), videoReport.getId()));
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(videoReport);
    }

    @RequiresRoles({Configs.SCOUTER_ROLE, Configs.ADMIN_ROLE})
    @DeleteMapping("videoReport")
    public ResponseEntity<?> deleteVideoReport(@RequestParam("id") Integer id) {
        String c = "VideoReportInterface";
        String m = "deleteVideoReport";
        logger.info(createLogMessage(c, m, "Start"));
        videoReportService.deleteVideoReport(id);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
