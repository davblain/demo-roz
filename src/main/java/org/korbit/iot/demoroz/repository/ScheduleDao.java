package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.Schedule;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ScheduleDao extends CrudRepository<Schedule,UUID> {
}
