package org.korbit.iot.demoroz.controllers;

import org.apache.log4j.Logger;
import org.korbit.iot.demoroz.dto.ChangePasswordDto;
import org.korbit.iot.demoroz.dto.UserDto;
import org.korbit.iot.demoroz.dto.UserProfileDto;
import org.korbit.iot.demoroz.dto.UserRegisterDto;
import org.korbit.iot.demoroz.exceptions.AlreadyExistException;
import org.korbit.iot.demoroz.exceptions.DeviceNotFoundException;
import org.korbit.iot.demoroz.exceptions.UserNotFoundException;
import org.korbit.iot.demoroz.exceptions.WrongPasswordException;
import org.korbit.iot.demoroz.models.User;
import org.korbit.iot.demoroz.secure.TokenUtils;
import org.korbit.iot.demoroz.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api")
public class TestController {
    final private UserService userService;
    final private Logger logger = Logger.getLogger(TestController.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private  AuthenticationManager authenticationManager;
    private TokenUtils tokenUtils;
    public TestController(UserService userService, BCryptPasswordEncoder e, AuthenticationManager authenticationManager,
                          TokenUtils tokenUtils, UserDetailsService userDetailsService){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenUtils = tokenUtils;
        this.bCryptPasswordEncoder = e;

    }
    @PostMapping("sign_up")
    @ResponseBody
    User registerUser(@RequestBody UserRegisterDto user) throws AlreadyExistException,DeviceNotFoundException {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return  userService.registerUser(user);
    }

    @PostMapping("sign_in")
    @ResponseBody
    String sign_in(@RequestBody UserRegisterDto user) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword() )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = this.userService.loadUserByUsername(user.getUsername());
        return this.tokenUtils.generateToken(userDetails);
    }
    @PutMapping("change_password")
    @ResponseBody
    String changePassword(@RequestBody ChangePasswordDto passwordDto, Authentication authentication)  throws WrongPasswordException{
        return userService.changePassword(authentication.getName(),passwordDto.getOldPassword(),passwordDto.getNewPassword());
    }

    @GetMapping("user")
    UserProfileDto getProfile(Authentication authentication)  throws UserNotFoundException{
        return  userService.getUserProfile(authentication.getName());
    }
    @GetMapping("user/{id}")
    UserDto getUser(@PathVariable  String id  ) {
        return userService.getUser(UUID.fromString(id));
    }




}
