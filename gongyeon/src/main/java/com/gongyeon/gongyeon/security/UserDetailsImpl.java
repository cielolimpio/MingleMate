package com.gongyeon.gongyeon.security;

import com.gongyeon.gongyeon.domain.Member;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;


@Getter
public class UserDetailsImpl extends User {

    private final Member member;

    public UserDetailsImpl(Member member) {
        super(member.getEmail(), member.getPassword(), member.getAuthorities());
        this.member = member;
    }
}