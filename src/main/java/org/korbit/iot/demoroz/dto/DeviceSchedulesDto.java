package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeviceSchedulesDto {
    List<ScheduleDto> schedules;
}
