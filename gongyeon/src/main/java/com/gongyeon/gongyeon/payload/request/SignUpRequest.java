package com.gongyeon.gongyeon.payload.request;

import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private GenderEnum gender;
    private int age;
    private Address address;
}
