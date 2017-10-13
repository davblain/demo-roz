package org.korbit.iot.demoroz.repository;

import org.korbit.iot.demoroz.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDao extends CrudRepository<User,UUID> {
    boolean existsByUsername(String username);
    User findUserByUsername(String name);

}
