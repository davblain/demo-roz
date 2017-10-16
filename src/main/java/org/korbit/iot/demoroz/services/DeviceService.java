package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.dto.DeviceDto;
import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.dto.ScheduleDto;
import org.korbit.iot.demoroz.dto.ScheduleWithIdDto;
import org.korbit.iot.demoroz.exceptions.DeviceNotFoundException;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.Schedule;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.repository.ScheduleDao;
import org.korbit.iot.demoroz.repository.UserDao;
import org.korbit.iot.demoroz.services.interfaces.OutboundChannel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DeviceService  {

    final private ScheduleDao scheduleDao;
    final private DeviceDao deviceDao;
    final private ModelMapper modelMapper;
    final private PushService pushService;
    final private UserDao userDao;
    final private OutboundChannel outboundChannel;
    public DeviceService(ScheduleDao scheduleDao, DeviceDao deviceDao, PushService pushService, UserDao userDao,
                         ModelMapper modelMapper, OutboundChannel channel) {
        this.scheduleDao = scheduleDao;
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.pushService = pushService;
        this.modelMapper = modelMapper;
        this.outboundChannel = channel;
    }
    public Iterable<Device> getDevices() {
        return deviceDao.findAll();
    }
    @Transactional
    public DeviceDto addSchedule(UUID deviceUUID, ScheduleDto schedule) {
        return Optional.ofNullable(deviceDao.findOne(deviceUUID))
                .map(d -> {
                    Schedule sch = modelMapper.map(schedule,Schedule.class);
                    sch.setDevice(d);
                    sch = scheduleDao.save(sch);
                    return  modelMapper.map(deviceDao.findOne(d.getUuid()),DeviceDto.class);
                }).orElseThrow(() -> new DeviceNotFoundException(deviceUUID.toString()));
    }
    public List<ScheduleWithIdDto> getSchedules(UUID device_uuid) {
        return Optional.ofNullable(deviceDao.findOne(device_uuid)).map(d -> d.getSchedules().stream()
        .map(s -> modelMapper.map(s,ScheduleWithIdDto.class))
                        .collect(Collectors.toList())
        ).orElseThrow(()-> new DeviceNotFoundException(device_uuid.toString()));
    }
    public boolean checkForOwner(String username,UUID uuid) {
        return userDao.findUserByUsername(username).getGroups().stream()
                .flatMap( g -> g.getDevices().stream())
                .anyMatch(d -> d.getUuid().equals(uuid));
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
