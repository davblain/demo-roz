package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.Schedule;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class Scheduler {
    private boolean state;
    private final ScheduleService scheduleService;
    private final DeviceService deviceService;
    public Scheduler(ScheduleService scheduleService,DeviceService deviceService) {
        this.scheduleService = scheduleService;
        this.deviceService = deviceService;
    }

    @Scheduled(fixedDelay = 50)
    void doIt() {
        LocalTime time = LocalTime.now();
       scheduleService.getSchedulesForScheduler(time).forEach( s -> {
           Device d = s.getDevice();
           if (!d.getPowState()&& time.isAfter(s.getBeginTime())&&time.isBefore(s.getEndTime())) {
               deviceService.switchPower(d.getUuid(),true);
           } else if (d.getPowState()&& (time.isBefore(s.getBeginTime())||(time.isAfter(s.getEndTime())))) {
               deviceService.switchPower(d.getUuid(),false);
           }
       }
       );
    }
}
