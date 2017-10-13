package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.dto.DeviceDto;
import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.dto.ScheduleDto;
import org.korbit.iot.demoroz.exceptions.DeviceNotFoundException;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.Schedule;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.repository.ScheduleDao;
import org.korbit.iot.demoroz.services.interfaces.OutboundChannel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService  {

    final private ScheduleDao scheduleDao;
    final private DeviceDao deviceDao;
    final private ModelMapper modelMapper;
    final private PushService pushService;
    final private OutboundChannel outboundChannel;
    public DeviceService(ScheduleDao scheduleDao, DeviceDao deviceDao, PushService pushService, ModelMapper modelMapper, OutboundChannel channel) {
        this.scheduleDao = scheduleDao;
        this.deviceDao = deviceDao;
        this.pushService = pushService;
        this.modelMapper = modelMapper;
        this.outboundChannel = channel;
    }
    public Iterable<Device> getDevices() {
        return deviceDao.findAll();
    }

    public DeviceDto addSchedule(UUID deviceUUID, ScheduleDto schedule) {
        return Optional.ofNullable(deviceDao.findOne(deviceUUID))
                .map(d -> {
                    Schedule sch = modelMapper.map(schedule,Schedule.class);
                    sch.setDevice(d);
                    sch = scheduleDao.save(sch);
                    return  modelMapper.map(deviceDao.findOne(d.getUuid()),DeviceDto.class);
                }).orElseThrow(() -> new DeviceNotFoundException(deviceUUID.toString()));
    }

    public void switchPower(UUID deviceUUID,Boolean power) {
        Optional.ofNullable(deviceDao.findOne(deviceUUID))
                .map(d -> {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("state",power.toString());
                    pushService.pushEvent(new OutEvent("state",deviceUUID.toString(),map), outboundChannel);
                    return d;
                }).orElseThrow(() -> new DeviceNotFoundException(deviceUUID.toString()));
    }

}
