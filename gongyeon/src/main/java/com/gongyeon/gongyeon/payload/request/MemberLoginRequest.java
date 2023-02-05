package com.gongyeon.gongyeon.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class MemberLoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
