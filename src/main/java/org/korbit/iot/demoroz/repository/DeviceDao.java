package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceDao extends JpaRepository<Device,UUID> {
    List<Device> findDevicesByOwner(Group group);
}
