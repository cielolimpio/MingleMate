package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.GenderEnum;

import com.gongyeon.gongyeon.enums.RoleEnum;
import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.Days;
import com.gongyeon.gongyeon.domain.embeddedTypes.Tags;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private int age;

    @Embedded
    private Address address;
    @Embedded
    private Days possibleDays;

    @OneToMany(mappedBy = "member")
    private List<StudyField> studyFields = new ArrayList<>();

    @Embedded
    private Tags tags;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;


    public static Member createMember(
            String name,
            String email,
            String password
    ) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = password;
        member.role = RoleEnum.USER;
        return member;
    }

    public void updateMember(
            GenderEnum gender,
            int age,
            Address address,
            Days possibleDays,
            StudyField... studyFields
    ) {
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.possibleDays = possibleDays;
        this.tags = new Tags();
        this.studyFields.addAll(Arrays.asList(studyFields));
    }

    public void changeRole(RoleEnum role){
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String role = this.getRole().toString();
        authorities.add( ()-> role);
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
