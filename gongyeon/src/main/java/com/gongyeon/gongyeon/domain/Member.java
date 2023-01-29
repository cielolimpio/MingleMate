package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.GenderEnum;

import com.gongyeon.gongyeon.enums.RoleEnum;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.LAZY;

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

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

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
