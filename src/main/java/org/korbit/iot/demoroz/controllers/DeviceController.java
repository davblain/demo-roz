package org.korbit.iot.demoroz.controllers;

import org.apache.log4j.Logger;
import org.korbit.iot.demoroz.dto.DeviceDto;
import org.korbit.iot.demoroz.dto.DeviceSchedulesDto;
import org.korbit.iot.demoroz.dto.ScheduleDto;
import org.korbit.iot.demoroz.dto.ScheduleWithIdDto;
import org.korbit.iot.demoroz.exceptions.DeviceNotFoundException;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.services.DeviceService;
import org.korbit.iot.demoroz.services.ScheduleService;
import org.korbit.iot.demoroz.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class DeviceController {
    final private UserService userService;
    final private DeviceDao deviceDao;
    final private ModelMapper modelMapper;
    final private ScheduleService scheduleService;
    final private DeviceService deviceService;
    final private Logger logger = Logger.getLogger(TestController.class);
    public DeviceController(UserService userService, DeviceDao deviceDao, DeviceService deviceService, ScheduleService scheduleService,
                            ModelMapper modelMapper){
        this.userService = userService;
        this.deviceDao = deviceDao;
        this.modelMapper = modelMapper;
        this.deviceService  = deviceService;
        this.scheduleService = scheduleService;
    }
    @RequestMapping(value = "device", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Device registerDevice(Authentication authentication) {
       return deviceDao.save(new Device());
    }
    @RequestMapping(value = "device", method = RequestMethod.GET)
    public List<DeviceDto> getDevices(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            return deviceService.getDevices();
        }
        return userService.getDevicesByUsername(authentication.getName());
    }

    @RequestMapping("device/{uuid}")
    @ResponseBody
    DeviceDto getDevice(@PathVariable String uuid,Authentication authentication ) {
        if ((authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))||
                deviceService.checkForOwner(authentication.getName(),UUID.fromString(uuid))) {
            return deviceService.getDeviceById(UUID.fromString(uuid));
        }
        throw   new AccessDeniedException("You have not permissions for get device");
    }
    @PostMapping("device/{uuid}/schedule")
    @ResponseBody
    DeviceSchedulesDto addSchedule(@PathVariable String uuid, @RequestBody @Validated ScheduleDto sch, Authentication authentication) {
        if ((authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))||
                deviceService.checkForOwner(authentication.getName(),UUID.fromString(uuid))) {
            return deviceService.addSchedule(UUID.fromString(uuid),sch);
        }

        throw   new AccessDeniedException("You have not permissions for add schedule");
    }
    @GetMapping("device/{uuid}/schedule")
    @ResponseBody
    List<ScheduleWithIdDto> getSchedules(@PathVariable String uuid, Authentication authentication) throws DeviceNotFoundException,AccessDeniedException{
        if ((authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))||
                deviceService.checkForOwner(authentication.getName(),UUID.fromString(uuid))){
            return deviceService.getSchedules(UUID.fromString(uuid));

        } else throw new AccessDeniedException("You have not permissions for get schedule");
    }

    @PutMapping("device/schedule/{schedule_uuid}")
    @ResponseBody
    ScheduleDto   updateSchedule(@PathVariable("schedule_uuid") String uuid,Authentication authentication,
                                 @RequestBody  @Validated ScheduleDto schedule) {
        if ((authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) ||
                (scheduleService.checkForOwner(authentication.getName(),UUID.fromString(uuid)))){
            return scheduleService.updateSchedule(UUID.fromString(uuid),schedule);
        }  else  throw  new AccessDeniedException("You have not permissions for update this schedule");
    }
    @DeleteMapping("device/schedule/{schedule_uuid}")
    @ResponseBody
    String deleteSchedule(@PathVariable("schedule_uuid") String uuid,Authentication authentication) {
        if ((authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) ||
                (scheduleService.checkForOwner(authentication.getName(),UUID.fromString(uuid)))){
            scheduleService.deleteSchedule(UUID.fromString(uuid));
            return "SUCCESS";
        }  else  throw  new AccessDeniedException("You have not permissions for delete this schedule");
    }

}
