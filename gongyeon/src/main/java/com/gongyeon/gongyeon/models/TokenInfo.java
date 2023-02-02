package com.gongyeon.gongyeon.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfo {

    private String grantType; // jwt 인증 타입 (Bearer 사용)
    private String accessToken;
    private String refreshToken;

}
