package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.dto.DeviceDto;
import org.korbit.iot.demoroz.dto.GroupDto;
import org.korbit.iot.demoroz.dto.UserDto;
import org.korbit.iot.demoroz.exceptions.AlreadyExistException;
import org.korbit.iot.demoroz.exceptions.DeviceNotFoundException;
import org.korbit.iot.demoroz.exceptions.GroupNotFoundException;
import org.korbit.iot.demoroz.exceptions.UserNotFoundException;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

        User user = Optional.ofNullable(userDao.findUserByUsername(username))
                .orElseThrow(()-> new UserNotFoundException(username));
        Group group = Optional.ofNullable(groupDao.findOne(uuid))
                .orElseThrow(()-> new GroupNotFoundException(uuid.toString()));
        group.getMembers().add(user);
        groupDao.save(group);
    }
    @Transactional
    public Device addDeviceToGroup(UUID deviceUUID,UUID groupUUID)  {
        Device device = Optional.ofNullable(deviceDao.findOne(deviceUUID))
                .orElseThrow(()-> new DeviceNotFoundException(deviceUUID.toString()));
        return Optional.ofNullable(groupDao.findOne(groupUUID)).map( g -> {
            device.setOwner(g);
            return deviceDao.save(device);
         }).orElseThrow(() -> new GroupNotFoundException(groupUUID.toString()));

    }
    @Transactional
    public List<DeviceDto> getDevices(UUID groupUUID) {
        return  Optional.ofNullable(groupDao.findOne(groupUUID)).map(g -> deviceDao.findDevicesByOwner(g).stream()
                    .map(d -> modelMapper.map(d,DeviceDto.class))
                    .collect(Collectors.toList()))
                .orElseThrow(()-> new GroupNotFoundException(groupUUID.toString()));

    }
    @Transactional
    public List<UserDto> getMembers(UUID uuid) {
        return Optional.ofNullable(groupDao.getOne(uuid)).map(g -> g.getMembers().stream()
                    .map(u -> modelMapper.map(u, UserDto.class))
                    .collect(Collectors.toList()))
                .orElseThrow(()-> new GroupNotFoundException(uuid.toString()));
    }


}
