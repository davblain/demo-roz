package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.models.Device;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    private boolean state;
    private final DeviceService deviceService;
    public Scheduler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Scheduled(fixedDelay = 100)
    void doIt() {
        Iterable<Device> devices = deviceService.getDevices();
        for (Device device: devices) {
            deviceService.switchPower(device.getUuid(),state);
            state = !state;
        }
    }
}
