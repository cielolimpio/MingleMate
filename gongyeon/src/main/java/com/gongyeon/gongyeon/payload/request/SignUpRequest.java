package com.gongyeon.gongyeon.payload.request;

import com.gongyeon.gongyeon.domain.Address;
import com.gongyeon.gongyeon.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embedded;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private GenderEnum gender;
    private int age;
    @Embedded
    private Address address;
}
