package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.Group;
import org.korbit.iot.demoroz.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GroupDao extends JpaRepository<Group,UUID> {

    Group findByAdmin(User user);
}
