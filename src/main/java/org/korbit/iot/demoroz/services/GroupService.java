package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.dto.GroupDto;
import org.korbit.iot.demoroz.exceptions.AlreadyExistException;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.Group;
import org.korbit.iot.demoroz.models.User;
import org.korbit.iot.demoroz.repository.DeviceDao;
import org.korbit.iot.demoroz.repository.GroupDao;
import org.korbit.iot.demoroz.repository.UserDao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebParam;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {
    final private GroupDao groupDao;
    final private UserDao userDao;
    final private DeviceDao deviceDao;
    final private ModelMapper modelMapper;
    public GroupService(UserDao userDao, GroupDao groupDao, DeviceDao deviceDao, ModelMapper modelMapper) {
        this.deviceDao = deviceDao;
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public GroupDto createGroup(User user, String name) {
        return user.getGroups().stream()
                .filter(g -> g.getAdmin().equals(user))
                .findAny().map(g -> modelMapper.map(g,GroupDto.class))
                .orElse( modelMapper.map(groupDao.save(new Group(user,name)),GroupDto.class));
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
