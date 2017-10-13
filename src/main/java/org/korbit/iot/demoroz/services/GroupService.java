package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.Group;
import org.korbit.iot.demoroz.models.User;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.repository.GroupDao;
import org.korbit.iot.demoroz.repository.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {
    final private GroupDao groupDao;
    final private UserDao userDao;
    final private DeviceDao deviceDao;
    public GroupService(UserDao userDao, GroupDao groupDao, DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
        this.groupDao = groupDao;
        this.userDao = userDao;
    }
    @Transactional
    public Group createGroup(User user,String name) {
        Group group = new Group(name);
        group.setAdmin(user);
        group.setMembers(Arrays.asList(user));
        return groupDao.save(group);
    }
    @Transactional
    public void addMember(UUID uuid, String username) {
        User user = userDao.findUserByUsername(username);
        Group group = groupDao.findOne(uuid);
        group.getMembers().add(user);
        groupDao.save(group);
    }
    @Transactional
    public Device addDeviceToGroup(UUID deviceUUID,UUID groupUUID) {
        Device device =  deviceDao.findOne(deviceUUID);
        Group  group = groupDao.findOne(groupUUID);
        device.setOwner(group);
        return deviceDao.save(device);
    }
    @Transactional
    public List<Device> getDevices(UUID groupUUID) {
        Group group = groupDao.findOne(groupUUID);
        return deviceDao.findDevicesByOwner(group);
    }


}
