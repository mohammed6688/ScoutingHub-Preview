package com.krnal.products.scoutinghub.events;

import com.krnal.products.scoutinghub.dto.CalendarDTO;
import com.krnal.products.scoutinghub.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CalendarEventListener {

    @Autowired
    CalendarService calendarService;

    @EventListener
    public void handleCalendarEvent(CalendarEvent event) {
        CalendarDTO calendarDTO = new CalendarDTO(event.getCreator(), event.getOperationType(), event.getResourceType(), event.getResourceId());
        calendarService.addInteraction(calendarDTO);
    }
}
