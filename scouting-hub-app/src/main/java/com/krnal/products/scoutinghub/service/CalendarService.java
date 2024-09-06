package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.dao.CalendarRepo;
import com.krnal.products.scoutinghub.dto.CalendarDTO;
import com.krnal.products.scoutinghub.mapper.CalendarMapper;
import com.krnal.products.scoutinghub.model.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Service
public class CalendarService {
    Logger logger = LoggerFactory.getLogger(CalendarService.class);

    @Autowired
    CalendarMapper calendarMapper;
    @Autowired
    private CalendarRepo calendarRepo;

    public List<CalendarDTO> getCalendar() {
        String c = "CalendarService";
        String m = "getCalendar";
        try {
            logger.info(createLogMessage(c, m, "Start"));

            List<CalendarDTO> calendarDTOList = calendarRepo.findAllBy().stream()
                    .map(calendar -> calendarMapper.getCalendarDTO(calendar))
                    .toList();

            logger.info(createLogMessage(c, m, "Success"));
            return calendarDTOList;
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    public void addInteraction(CalendarDTO calendarDTO) {
        String c = "CalendarService";
        String m = "addInteraction";
        try {
            logger.info(createLogMessage(c, m, "Start"));

            Calendar calendar = calendarRepo.save(calendarMapper.getCalendar(calendarDTO));
            calendarMapper.getCalendarDTO(calendar);

            logger.info(createLogMessage(c, m, "Success"));
        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }
}
