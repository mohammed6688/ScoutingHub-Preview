package com.krnal.products.scoutinghub.mapper;

import com.krnal.products.scoutinghub.dto.CalendarDTO;
import com.krnal.products.scoutinghub.dto.PositionDTO;
import com.krnal.products.scoutinghub.model.Calendar;
import com.krnal.products.scoutinghub.model.Position;
import org.mapstruct.Mapper;

@Mapper
public interface CalendarMapper {
    Calendar getCalendar(CalendarDTO calendarDTO);
    CalendarDTO getCalendarDTO(Calendar calendar);
}
