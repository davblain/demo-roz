package org.korbit.iot.demoroz.dto.input;

import java.util.UUID;

public class PowerChangeDto extends MessageDto {
    private Boolean state;
    public PowerChangeDto() {

    }

    public PowerChangeDto(UUID uuid, Boolean state) {
        super(uuid);
        this.state = state;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
