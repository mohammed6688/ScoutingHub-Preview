package com.krnal.products.scoutinghub.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarEvent {
    private String creator;
    private Integer operationType;
    private Integer resourceType;
    private Integer resourceId;
}
