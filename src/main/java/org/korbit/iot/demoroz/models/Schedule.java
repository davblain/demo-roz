package org.korbit.iot.demoroz.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue
    @Column(name = "uuid")
    private UUID uuid;
    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    private Device device;
    @Column
    private LocalTime beginTime;
    @Column
    private LocalTime endTime;
    @Column
    private Boolean active;
    public Schedule() {
        active = true;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (uuid != null ? !uuid.equals(schedule.uuid) : schedule.uuid != null) return false;
        return device != null ? device.equals(schedule.device) : schedule.device == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (device != null ? device.hashCode() : 0);
        return result;
    }
}
