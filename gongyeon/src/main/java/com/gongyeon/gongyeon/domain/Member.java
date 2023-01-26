package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.GenderEnum;
import com.gongyeon.gongyeon.enums.RoleEnum;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "members")
@Getter
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
