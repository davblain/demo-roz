package org.korbit.iot.demoroz.mqtt;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MqttController {
    private final ApplicationEventPublisher publisher;

    public MqttController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    void handleMessage(String topic, String data) {

        System.out.println(topic);
    }
}
