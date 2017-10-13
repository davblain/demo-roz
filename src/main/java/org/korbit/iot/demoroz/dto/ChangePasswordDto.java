package org.korbit.iot.demoroz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    String oldPassword;
    String newPassword;

}
