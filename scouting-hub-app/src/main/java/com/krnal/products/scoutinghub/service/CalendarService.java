package com.krnal.products.scoutinghub.service;

import com.krnal.products.scoutinghub.configs.Configs;
import com.krnal.products.scoutinghub.dao.CalendarRepo;
import com.krnal.products.scoutinghub.dto.CalendarDTO;
import com.krnal.products.scoutinghub.mapper.CalendarMapper;
import com.krnal.products.scoutinghub.model.Calendar;
import com.krnal.products.scoutinghub.security.UserSessionHelper;
import com.krnal.products.scoutinghub.specification.CalendarDecorator;
import com.krnal.products.scoutinghub.specification.SimpleSpecification;
import com.krnal.products.scoutinghub.types.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.krnal.products.scoutinghub.utils.LogUtils.createLogMessage;

@Service
public class CalendarService {
    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private final CalendarMapper calendarMapper;
    private final CalendarRepo calendarRepo;

    public CalendarService(CalendarMapper calendarMapper, CalendarRepo calendarRepo) {
        this.calendarMapper = calendarMapper;
        this.calendarRepo = calendarRepo;
    }

    public List<CalendarDTO> getCalendarEvents() {
        String c = "CalendarService";
        String m = "getCalendar";
        try {
            logger.info(createLogMessage(c, m, "Start"));

            List<CalendarDTO> calendarDTOList = calendarRepo.findAllBy().stream()
                    .map(calendarMapper::getCalendarDTO)
                    .filter(calendar -> !(UserSessionHelper.checkUserAccess(Configs.SCOUTER_ROLE)) || calendar.getCreator().equals(UserSessionHelper.getUserId()))
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

    public List<CalendarDTO> searchCalender(List<SearchCriteria> params) {
        String c = "CalendarService";
        String m = "searchCalender";

        try {
            logger.info(createLogMessage(c, m, "Start"));

            if (params == null || params.isEmpty()) {
                return getCalendarEvents();
            }
            Specification<Calendar> spec = Specification.where(createSpecification(params.getFirst()));

            for (int i = 1; i < params.size(); i++) {
                spec = spec.and(createSpecification(params.get(i)));
            }

            List<Calendar> calendarList = calendarRepo.findAll(spec);
            return calendarList.stream()
                    .map(calendarMapper::getCalendarDTO)
                    .toList();

        } catch (Exception e) {
            logger.error(createLogMessage(c, m, "error", "message", e.getMessage()));
            throw new RuntimeException(e.getMessage(), e); // Re-throw as unchecked exception
        }
    }

    private Specification<Calendar> createSpecification(SearchCriteria criteria) {
        Specification<Calendar> specification = new SimpleSpecification<>(criteria);
        return new CalendarDecorator(specification);
    }
}
