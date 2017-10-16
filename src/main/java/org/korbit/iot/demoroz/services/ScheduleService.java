package org.korbit.iot.demoroz.services;


import org.korbit.iot.demoroz.dto.ScheduleDto;
import org.korbit.iot.demoroz.exceptions.ScheduleNotFoundException;
import org.korbit.iot.demoroz.models.Schedule;
import org.korbit.iot.demoroz.repository.ScheduleDao;
import org.korbit.iot.demoroz.repository.UserDao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    final private ScheduleDao scheduleDao;
    final private ModelMapper modelMapper;
    final private UserDao userDao;
    public ScheduleService(ScheduleDao scheduleDao,UserDao userDao,ModelMapper modelMapper) {
        this.scheduleDao = scheduleDao;
        this.modelMapper = modelMapper;
        this.userDao = userDao;
    }
    public List<Schedule> getAllSchedules () {
        return scheduleDao.findAll();
    }
    @Transactional
    public List<Schedule> getSchedulesForScheduler() {
        return scheduleDao.findByActiveIsTrue();//.stream().filter( schedule ->
                //time.isAfter(schedule.getBeginTime()) && time.isBefore(schedule.getEndTime())).collect(Collectors.toList());
          // ChronoUnit.MILLIS.between(time,schedule.getBeginTime())< 50|| ChronoUnit.MILLIS.between(time,schedule.getEndTime())<50).collect(Collectors.toList());
    }

    public Schedule deactivate(UUID uuid) {
        return Optional.ofNullable(scheduleDao.findOne(uuid)).map(sch -> {
            sch.setActive(false);
            return scheduleDao.save(sch);
        }).orElseThrow(()->new ScheduleNotFoundException(uuid.toString()));
    }
    public Schedule activate(UUID uuid) {
        return Optional.ofNullable(scheduleDao.findOne(uuid)).map(sch -> {
            sch.setActive(true);
            return scheduleDao.save(sch);
        }).orElseThrow(()->new ScheduleNotFoundException(uuid.toString()));
    }

    public ScheduleDto updateSchedule( UUID uuid,ScheduleDto schedule) {
        return Optional.ofNullable(scheduleDao.findOne(uuid)).map( s -> {
            modelMapper.map(schedule, s);
            scheduleDao.save(s);
            return schedule;
        }).orElseThrow(() -> new ScheduleNotFoundException(uuid.toString()));
    }
    @Transactional
    public  boolean checkForOwner(String username,UUID scheduleUUID) {
       return userDao.findUserByUsername(username).getGroups().stream()
               .flatMap( g -> g.getDevices().stream())
               .flatMap(d -> d.getSchedules().stream())
               .anyMatch(s -> s.getUuid().equals(scheduleUUID));
    }
    @Transactional
    public void deleteSchedule(UUID uuid) {
        scheduleDao.delete(uuid);
    }



}
