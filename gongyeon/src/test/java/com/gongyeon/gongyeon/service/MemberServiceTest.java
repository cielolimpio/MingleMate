package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.GongyeonBaseTest;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.models.TokenInfo;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class MemberServiceTest extends GongyeonBaseTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void signUp() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        TokenInfo tokenInfo = memberService.signUp(member);

        //when
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        String email = authentication.getName();
        Assertions.assertThat(email).isEqualTo("test1@gmail.com");

        //then
        Optional<Member> memberFound = memberRepository.findByEmail(email);
        Assertions.assertThat(memberFound).isPresent();
        Assertions.assertThat(memberFound.get().getName()).isEqualTo("test1");
    }

    @Test
    public void 회원가입_이메일_중복() throws Exception {
        //given
        Member member1 = Member.createMember("test1", "test1@gmail.com", "test1");
        memberService.signUp(member1);
        Member member2 = Member.createMember("test2", "test1@gmail.com", "test2");

        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.signUp(member2);
        });
    }
}
