package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.Group;
import org.korbit.iot.demoroz.models.Role;
import org.korbit.iot.demoroz.models.User;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.repository.GroupDao;
import org.korbit.iot.demoroz.repository.RoleDao;
import org.korbit.iot.demoroz.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserDao userRepository;

    @Autowired
    private RoleDao roleRepository;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!alreadySetup) {

            createRoleIfNotFound("ROLE_ADMIN");
            createRoleIfNotFound("ROLE_USER");
            Role userRole = roleRepository.findByName("ROLE_USER");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("587238"));
            user.setRoles(Arrays.asList(adminRole,userRole));
            user = userRepository.save(user);
            Group group = groupDao.save(new Group(user,""));
            Device device = deviceDao.save(new Device());
            device.setOwner(group);
            deviceDao.save(device);
            user = new User();
            user.setUsername("davblain");
            user.setPassword(passwordEncoder.encode("pass"));
            user.setRoles(Arrays.asList(userRole));
            user.setGroups(Arrays.asList(group));
            userRepository.save(user);
            alreadySetup = true;
        }

    }


    @Transactional
    public Role createRoleIfNotFound(String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }
}