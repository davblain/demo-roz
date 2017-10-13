package org.korbit.iot.demoroz.services;


import org.korbit.iot.demoroz.exceptions.ScheduleNotFoundException;
import org.korbit.iot.demoroz.models.Schedule;
import org.korbit.iot.demoroz.repository.ScheduleDao;
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
    public ScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
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
    Schedule deactivate(UUID uuid) {
        return Optional.ofNullable(scheduleDao.findOne(uuid)).map(sch -> {
            sch.setActive(false);
            return scheduleDao.save(sch);
        }).orElseThrow(()->new ScheduleNotFoundException(uuid.toString()));
    }
    Schedule activate(UUID uuid) {
        return Optional.ofNullable(scheduleDao.findOne(uuid)).map(sch -> {
            sch.setActive(true);
            return scheduleDao.save(sch);
        }).orElseThrow(()->new ScheduleNotFoundException(uuid.toString()));
    }


}
