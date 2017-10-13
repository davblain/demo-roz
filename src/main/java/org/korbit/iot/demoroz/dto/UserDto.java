package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;
import org.korbit.iot.demoroz.models.Group;

@Getter
@Setter
public class UserDto {
    String id;
    String username;
    Group group;

}
