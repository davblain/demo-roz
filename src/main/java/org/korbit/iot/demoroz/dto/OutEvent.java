package org.korbit.iot.demoroz.dto;

import java.util.Map;

public class OutEvent {
    private String type;
    private String uuid;
    private Map<String,String> source;

    public Map<String,String> getSource() {
        return source;
    }

    public OutEvent(String type, String uuid,Map<String,String> source) {
        this.type = type;
        this.uuid = uuid;
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
