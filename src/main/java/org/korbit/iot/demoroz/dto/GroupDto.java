package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GroupDto {
    UUID uuid;
    String admin;
    String name;
    List<String> devices;
}
