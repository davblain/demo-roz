package org.korbit.iot.demoroz.events;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class PowerChangeEvent extends ApplicationEvent {
    private Boolean pow_state;
    private UUID device_uuid;
    public PowerChangeEvent(Object o, UUID device_uuid,Boolean state) {
        super(o);
        this.pow_state = state;
        this.device_uuid = device_uuid;
    }

    public Boolean getPow_state() {
        return pow_state;
    }

    public UUID getDevice_uuid() {
        return device_uuid;
    }
}
