package org.korbit.iot.demoroz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "device")
@Getter
@Setter
public class Device {
    @Id
    @GeneratedValue
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "pow_state")
    private Boolean powState;
    @Column(name = "temperature")
    private Integer temperature;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Group owner;
    @OneToMany(mappedBy = "device")
    private List<Schedule> schedules;

    public Device() {
        powState = false;
    }


    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        if (uuid != null ? !uuid.equals(device.uuid) : device.uuid != null) return false;
        if (powState != null ? !powState.equals(device.powState) : device.powState != null) return false;
        return owner != null ? owner.equals(device.owner) : device.owner == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (powState != null ? powState.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
