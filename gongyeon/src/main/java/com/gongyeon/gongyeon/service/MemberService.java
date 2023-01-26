package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.security.JwtTokenProvider;
import com.gongyeon.gongyeon.security.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

//    public Long signUp(Member member) {
//        validateDuplicateMember(member);
//        Member newMember = memberRepository.save(member);
//        return newMember.getId();
//    }

//    private void validateDuplicateMember(Member member) {
//        List<Member> members = memberRepository.findByEmail(member.getEmail());
//        if (!members.isEmpty())
//            throw new GongYeonException(HttpStatusEnum.CONFLICT, "Duplicate Member");
//    }

    public TokenInfo login(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }
}
