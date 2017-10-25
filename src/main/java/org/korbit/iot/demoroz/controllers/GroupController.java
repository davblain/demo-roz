package org.korbit.iot.demoroz.controllers;

import org.korbit.iot.demoroz.dto.DeviceDto;
import org.korbit.iot.demoroz.dto.GroupDto;
import org.korbit.iot.demoroz.exceptions.UserNotFoundException;
import org.korbit.iot.demoroz.models.Device;
import org.korbit.iot.demoroz.models.User;
import org.korbit.iot.demoroz.services.GroupService;
import org.korbit.iot.demoroz.services.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class GroupController {

    final private GroupService groupService;
    final private UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @PostMapping("group")
    @ResponseBody
    GroupDto createGroup(Authentication authentication, @RequestBody String name) {
        return groupService.createGroup(userService.getUserByUsername(authentication.getName()),name);
    }
    @GetMapping("group/{id}")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    GroupDto getGroup(@PathVariable(name = "id") String uuid,Authentication authentication) {
        if (groupService.getMembers(UUID.fromString(uuid)).stream().anyMatch( u -> u.getUsername().equals(authentication.getName()))) {
            return groupService.getGroupById(UUID.fromString(uuid));
        }
        else  throw  new AccessDeniedException("You have not permissions for get group");
    }
    @RequestMapping(value = "group/{uuid}/devices", method = RequestMethod.GET)
    public List<DeviceDto> getDevicesOfGroup( @PathVariable(name = "id") String uuid,Authentication authentication) {
        if (groupService.getMembers(UUID.fromString(uuid)).stream().anyMatch( u -> u.getUsername().equals(authentication.getName()))) {
            return groupService.getDevices(UUID.fromString(uuid));
        }
        else  throw  new AccessDeniedException("You have not permissions for get group");
    }
    @PostMapping("group/member")
    @ResponseBody
    String addMember(Authentication authentication,@RequestParam String username) throws UserNotFoundException {
       GroupDto group =  userService.getAdministratedGroup(authentication.getName());
       groupService.addMember(group.getUuid(),username);
       return "SUCCESS";
    }
    @DeleteMapping("group/member")
    @ResponseBody
    String deleteMember(Authentication authentication, @RequestParam String  username) throws UserNotFoundException {
        GroupDto group =  userService.getAdministratedGroup(authentication.getName());
        groupService.deleteMember(group.getUuid(),username);
        return "SUCCESS";
    }

}
