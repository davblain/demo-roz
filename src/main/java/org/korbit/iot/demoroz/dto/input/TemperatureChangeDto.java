package org.korbit.iot.demoroz.dto.input;

import java.util.UUID;

public class TemperatureChangeDto extends MessageDto {

    private Integer temperature;
    public TemperatureChangeDto() {

    }
    public TemperatureChangeDto(UUID uuid, Integer temperature) {
        super(uuid);
        this.temperature = temperature;
    }


    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
}
