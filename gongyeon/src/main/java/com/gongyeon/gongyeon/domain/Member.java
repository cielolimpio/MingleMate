package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.GenderEnum;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "members")
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


    public static Member createMember(String name, String email, String password, GenderEnum gender, int age, Address address) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = password;
        member.gender = gender;
        member.age = age;
        member.address = address;

        return member;
    }
}
