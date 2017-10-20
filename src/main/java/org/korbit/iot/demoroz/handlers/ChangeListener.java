package org.korbit.iot.demoroz.handlers;

import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.events.PowerChangeEvent;
import org.korbit.iot.demoroz.events.TemperatureChangeEvent;
import org.korbit.iot.demoroz.exceptions.DeviceNotFoundException;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.services.interfaces.OutboundChannel;
import org.korbit.iot.demoroz.websock.WebSocketOutboundChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Component
@Transactional
public class ChangeListener {

    private final DeviceDao deviceDao;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final  OutboundChannel webSocketChannel;

    public ChangeListener(DeviceDao deviceDao,  @Qualifier("webSocketOutbound") OutboundChannel outboundChannel, SimpMessagingTemplate simpMessagingTemplate) {
        this.deviceDao = deviceDao;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.webSocketChannel = outboundChannel;
    }

    @EventListener
    void handle(TemperatureChangeEvent e) throws DeviceNotFoundException {
        Device device = Optional.ofNullable(deviceDao.findOne(e.getDevice_uuid()))
                .orElseThrow(()-> new DeviceNotFoundException(e.getDevice_uuid().toString()));
        device.setTemperature(e.getTemperature());
        deviceDao.save(device);
        HashMap<String,String> source = new HashMap<>();
        source.put("temp",e.getTemperature().toString());
        webSocketChannel.pushEvent(new OutEvent("temp",e.getDevice_uuid().toString(),source));

    }
    @EventListener
    void handle(PowerChangeEvent e) throws  DeviceNotFoundException {
        Device device = Optional.ofNullable(deviceDao.findOne(e.getDevice_uuid()))
                .orElseThrow(()-> new DeviceNotFoundException(e.getDevice_uuid().toString()));
        device.setPowState(e.getPow_state());
        deviceDao.save(device);
        HashMap<String,String> source = new HashMap<>();
        source.put("pow",e.getPow_state().toString());
        new OutEvent("pow",e.getDevice_uuid().toString(),source);
    }

}
