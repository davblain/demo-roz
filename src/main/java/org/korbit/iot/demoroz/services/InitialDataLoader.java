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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {
    private  @Value("${spring.jpa.hibernate.ddl-auto}") String policy;
    @Value("${load.already-setup}") String  alreadySetup;

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

        if (!alreadySetup.equals("true")) {

            createRoleIfNotFound("ROLE_ADMIN");
            createRoleIfNotFound("ROLE_USER");
            Role userRole = roleRepository.findByName("ROLE_USER");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            User user1 = new User();
            user1.setUsername("admin");
            user1.setPassword(passwordEncoder.encode("587238"));
            user1.setRoles(Arrays.asList(adminRole,userRole));
            user1 = userRepository.save(user1);
            Group group =  new Group(user1,"");
            Device device = deviceDao.save(new Device());
            group = groupDao.save(group);
            device.setOwner(group);
            deviceDao.save(device);
            User user2 = new User();
            user2.setUsername("davblain");
            user2.setPassword(passwordEncoder.encode("pass"));
            user2.setRoles(Arrays.asList(userRole));
            user2.getGroups().add(group);
            userRepository.save(user2);

            alreadySetup = "true";
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