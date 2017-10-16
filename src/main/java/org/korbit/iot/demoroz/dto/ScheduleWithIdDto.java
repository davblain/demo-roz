package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class ScheduleWithIdDto  extends  ScheduleDto{
    UUID uuid;

}
