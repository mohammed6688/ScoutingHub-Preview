package com.krnal.products.scoutinghub.api;

import com.krnal.products.scoutinghub.dto.CalendarDTO;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.service.CalendarService;
import com.krnal.products.scoutinghub.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

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
        List<CalendarDTO> calendarDTOList = calendarService.getCalendar();
        logger.info(createLogMessage(c, m, "Success", "positionsSize", String.valueOf(calendarDTOList.size())));
        return ResponseEntity.status(HttpStatus.OK).body(calendarDTOList);
    }

}
