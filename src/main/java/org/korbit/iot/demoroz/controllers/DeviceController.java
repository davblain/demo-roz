package org.korbit.iot.demoroz.controllers;

import org.apache.log4j.Logger;
import org.korbit.iot.demoroz.dto.DeviceDto;
import org.korbit.iot.demoroz.dto.ScheduleDto;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.services.DeviceService;
import org.korbit.iot.demoroz.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api")
public class DeviceController {
    final private UserService userService;
    final private DeviceDao deviceDao;
    final private ModelMapper modelMapper;
    final private DeviceService deviceService;
    final private Logger logger = Logger.getLogger(TestController.class);
    public DeviceController(UserService userService, DeviceDao deviceDao, DeviceService deviceService, ModelMapper modelMapper){
        this.userService = userService;
        this.deviceDao = deviceDao;
        this.modelMapper = modelMapper;
        this.deviceService  = deviceService;
    }
    @RequestMapping(value = "device", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Device registerDevice(Authentication authentication) {
       return deviceDao.save(new Device());
    }
    @RequestMapping(value = "device", method = RequestMethod.GET)
    public Iterable<Device> getDevices(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            return deviceService.getDevices();
        }
        return userService.getDevicesByUsername(authentication.getName());
    }




    @RequestMapping("device/{uuid}/schedule")
    DeviceDto addSchedule(@PathVariable String uuid, @RequestBody @Validated ScheduleDto sch) {
        return deviceService.addSchedule(UUID.fromString(uuid),sch);
    }
}
