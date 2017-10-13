package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.Group;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GroupDao extends CrudRepository<Group,UUID> {
}
