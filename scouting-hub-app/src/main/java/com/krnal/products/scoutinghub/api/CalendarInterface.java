package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.dto.CalendarDTO;
import com.krnal.products.scoutinghub.service.CalendarService;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@RestController
@RequestMapping("/api")
public class CalendarInterface {
    Logger logger = LoggerFactory.getLogger(CalendarInterface.class);

    @Autowired
    CalendarService calendarService;

    @GetMapping("calendar")
    public ResponseEntity<List<CalendarDTO>> getAll() {
        String c = "CalendarInterface";
        String m = "getAll";
        logger.info(createLogMessage(c, m, "Start"));
        List<CalendarDTO> calendarDTOList = calendarService.getCalendarEvents();
        logger.info(createLogMessage(c, m, "Success", "positionsSize", String.valueOf(calendarDTOList.size())));
        return ResponseEntity.status(HttpStatus.OK).body(calendarDTOList);
    }

    @PostMapping("calendar/search")
    public ResponseEntity<?> searchCalender(
            @RequestBody List<SearchCriteria> searchCriteriaList) {
        String c = "CalendarInterface";
        String m = "searchCalender";
        logger.info(createLogMessage(c, m, "Start"));
        List<CalendarDTO> calenderEvents = calendarService.searchCalender(searchCriteriaList);
        logger.info(createLogMessage(c, m, "Success"));
        return ResponseEntity.status(HttpStatus.OK).body(calenderEvents);
    }
}
