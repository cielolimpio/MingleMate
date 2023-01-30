package com.gongyeon.gongyeon.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberLoginRequest {
    private String email;
    private String password;
}
