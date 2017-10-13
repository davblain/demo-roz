package org.korbit.iot.demoroz.handlers;

import org.korbit.iot.demoroz.events.PowerChangeEvent;
import org.korbit.iot.demoroz.events.TemperatureChangeEvent;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ChangeListener {

    private final DeviceDao deviceDao;
    public ChangeListener(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    @EventListener
    void handle(TemperatureChangeEvent e) {
        Device device = deviceDao.findOne(e.getDevice_uuid());
        device.setTemperature(e.getTemperature());
        deviceDao.save(device);
    }
    @EventListener
    void handle(PowerChangeEvent e) {
        Device device = deviceDao.findOne(e.getDevice_uuid());
        device.setPowState(e.getPow_state());
        deviceDao.save(device);
    }
}
