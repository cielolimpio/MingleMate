package com.minglemate.minglemate.models.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class MemberLoginRequest {
    @NotBlank(message = "email은 필수값 입니다.")
    private String email;
    @NotBlank(message = "password는 필수값 입니다.")
    private String password;
}
