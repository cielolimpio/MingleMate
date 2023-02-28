package com.minglemate.minglemate.models.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "name은 필수값 입니다.")
    private String name;
    @NotBlank(message = "email은 필수값 입니다.")
    private String email;
    @NotBlank(message = "password는 필수값 입니다.")
    private String password;
}
