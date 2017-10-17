package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DeviceDto extends DeviceData{
    UUID uuid;
    List<String> schedules;

    public DeviceDto() {
    }
}
