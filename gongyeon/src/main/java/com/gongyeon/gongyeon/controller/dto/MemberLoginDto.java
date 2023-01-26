package com.gongyeon.gongyeon.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberLoginDto {
    private String email;
    private String password;
}
