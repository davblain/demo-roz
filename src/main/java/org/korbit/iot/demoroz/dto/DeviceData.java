package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class DeviceData {
    private Boolean powState;
    private Integer temperature;

    public DeviceData() {
    }
}
