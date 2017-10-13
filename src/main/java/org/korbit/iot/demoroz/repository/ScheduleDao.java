package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleDao extends JpaRepository<Schedule,UUID> {
    List<Schedule> findByActiveIsTrue();
}
