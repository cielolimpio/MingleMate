package com.mingleMate.mingleMate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
public class TokenInfo {

    @NotBlank
    private String grantType; // jwt 인증 타입 (Bearer 사용)
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
