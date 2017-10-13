package org.korbit.iot.demoroz.dto.input;

public class TemperatureChangeDto extends MessageDto {

    private Integer temperature;
    public TemperatureChangeDto() {

    }


    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
}
