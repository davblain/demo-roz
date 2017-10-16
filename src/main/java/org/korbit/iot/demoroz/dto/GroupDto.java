package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupDto {
    String uuid;
    String admin;
    String name;
    List<String> devices;
}
