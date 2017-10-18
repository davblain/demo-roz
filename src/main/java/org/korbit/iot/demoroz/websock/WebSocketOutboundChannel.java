package org.korbit.iot.demoroz.websock;

import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.services.interfaces.OutboundChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("webSocketOutbound")
public class WebSocketOutboundChannel implements OutboundChannel{


    final private SimpMessagingTemplate simpMessagingTemplate;
    final private DeviceDao deviceDao;
    WebSocketOutboundChannel(SimpMessagingTemplate simpMessagingTemplate, DeviceDao deviceDao) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.deviceDao = deviceDao;
    }


    @Override
    public void pushEvent(OutEvent event) {
        deviceDao.findOne(UUID.fromString(event.getUuid())).getOwner().getMembers()
                .forEach(m -> simpMessagingTemplate.convertAndSendToUser(m.getUsername(),"/queue/reply",event));
    }


}
