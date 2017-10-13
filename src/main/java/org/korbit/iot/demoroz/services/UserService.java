package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.dto.UserDto;
import org.korbit.iot.demoroz.dto.UserProfileDto;
import org.korbit.iot.demoroz.dto.UserRegisterDto;
import org.korbit.iot.demoroz.exceptions.AlreadyExistException;
import org.korbit.iot.demoroz.exceptions.DeviceNotFoundException;
import org.korbit.iot.demoroz.exceptions.UserNotFoundException;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.User;
import org.korbit.iot.demoroz.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class UserService  implements UserDetailsService{
    final private UserDao userDao;
    final private  DeviceDao deviceDao;
    final private RoleDao roleDao;
    final private GroupDao groupDao;
    final private ModelMapper modelMapper;
    UserService(UserDao userDao, DeviceDao deviceDao, RoleDao roleDao, GroupDao groupDao, ModelMapper modelMapper) {
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.roleDao = roleDao;
        this.groupDao = groupDao;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public User registerUser(UserRegisterDto userRegisterDto)  throws  AlreadyExistException, DeviceNotFoundException{
        if (userDao.existsByUsername(userRegisterDto.getUsername())) throw  new AlreadyExistException(userRegisterDto.getUsername());
        User user =  modelMapper.map(userRegisterDto,User.class);
        user.setRoles(Arrays.asList(roleDao.findByName("ROLE_USER")));
        return  userDao.save(user);


    }

    @Transactional
    public User getUserByUsername(String name) throws  UserNotFoundException {
        return Optional.ofNullable(userDao.findUserByUsername(name)).orElseThrow(() -> new UserNotFoundException(name) );
    }
    @Transactional
    public UserProfileDto getUserProfile(String name) throws UserNotFoundException {

        return modelMapper.map(getUserByUsername(name),UserProfileDto.class);
    }
    @Transactional
    public Iterable<Device> getDevicesByUsername(String name) throws  UserNotFoundException{
     User user = getUserByUsername(name);
     return user.getGroups().stream().flatMap( group -> group.getDevices().stream()).collect(Collectors.toList());
    }

    public UserDto getUser(UUID uuid) {
       return  modelMapper.map(userDao.findOne(uuid), UserDto.class);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  getUserByUsername(username);
    }

}
