package org.korbit.iot.demoroz.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;
@Getter
@Setter
public class TemperatureChangeEvent extends ApplicationEvent {
    private UUID device_uuid;
    private Integer temperature;
    public TemperatureChangeEvent(Object source, UUID uuid, Integer temperature ) {
        super(source);
        this.device_uuid = uuid;
        this.temperature = temperature;
    }


}
