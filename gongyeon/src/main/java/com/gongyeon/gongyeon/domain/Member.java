package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.GenderEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private int age;

    @Embedded
    private Address address;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "possible_day_id")
    private PossibleDay possibleDay;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_tag_id")
    private MemberTag memberTag;

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
