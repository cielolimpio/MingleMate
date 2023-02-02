package com.gongyeon.gongyeon.security;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@SuperBuilder
public class UserDetailsImpl extends User {
    private Long memberId;
    private String name;

    public UserDetailsImpl(
            Long memberId,
            String email,
            String password,
            String name,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(email, password, authorities);
        this.memberId = memberId;
        this.name = name;
    }
}