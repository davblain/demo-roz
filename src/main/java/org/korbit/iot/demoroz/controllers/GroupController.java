package org.korbit.iot.demoroz.controllers;

import org.korbit.iot.demoroz.dto.GroupDto;
import org.korbit.iot.demoroz.services.GroupService;
import org.korbit.iot.demoroz.services.UserService;
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

    @PostMapping("groups")
    @ResponseBody
    GroupDto createGroup(Authentication authentication, @RequestBody String name) {
        return groupService.createGroup(userService.getUserByUsername(authentication.getName()),name);
    }
    @GetMapping("groups/{id}")
    @ResponseBody
    List<String> getDevicesId(@PathVariable(name = "id") String uuid) {
        return groupService.getDevices(UUID.fromString(uuid)).stream().map(d -> d.getUuid().toString()).collect(Collectors.toList());
    }




}
