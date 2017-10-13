package org.korbit.iot.demoroz.dto.input;

import java.util.UUID;

public class MessageDto  {
    protected UUID uuid;
    public MessageDto() {
    }

    public MessageDto(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
