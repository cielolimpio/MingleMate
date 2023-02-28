package com.minglemate.minglemate.security;

import com.minglemate.minglemate.domain.Member;
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