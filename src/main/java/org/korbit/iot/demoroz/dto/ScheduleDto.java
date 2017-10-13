package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
@Getter
@Setter
public class ScheduleDto {
    public ScheduleDto(){

    }
    @NotNull
    LocalTime beginTime;
    @NotNull
    LocalTime endTime;
}
