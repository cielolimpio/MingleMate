package com.minglemate.minglemate.domain;

import com.minglemate.minglemate.enums.GenderEnum;

import com.minglemate.minglemate.enums.RoleEnum;
import com.minglemate.minglemate.domain.embeddedTypes.Address;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 20)
    private String name;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 200)
    private String password;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private int age;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<ProfileImage> profileImages = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberCategory> memberCategories = new ArrayList<>();

    @Column(nullable = false, length = 10)
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
            String name,
            GenderEnum gender,
            int age,
            Address address,
            List<MemberCategory> memberCategories
    ) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.memberCategories.addAll(memberCategories);
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
