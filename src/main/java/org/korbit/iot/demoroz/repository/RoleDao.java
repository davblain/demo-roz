package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends CrudRepository<Role,Long> {
     Role findByName(String name);
}
