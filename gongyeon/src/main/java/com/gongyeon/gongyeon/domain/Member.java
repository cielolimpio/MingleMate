package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.GenderEnum;
import lombok.Getter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String password;

    private GenderEnum gender;
    private int age;

    @Embedded
    private Address address;
}
