package org.korbit.iot.demoroz.services;


import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.services.interfaces.OutboundChannel;
import org.springframework.stereotype.Component;

@Component
public class PushService {

    public  void pushEvent(OutEvent event, OutboundChannel channel) {
        channel.pushEvent(event);
    }
}
