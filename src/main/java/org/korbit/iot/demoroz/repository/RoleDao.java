package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends JpaRepository<Role,Long> {
     Role findByName(String name);
}
