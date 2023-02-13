package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.GongyeonBaseTest;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.domain.RefreshToken;
import com.gongyeon.gongyeon.domain.StudyField;
import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.DaysOfTheWeek;
import com.gongyeon.gongyeon.enums.CategoryEnum;
import com.gongyeon.gongyeon.enums.GenderEnum;
import com.gongyeon.gongyeon.models.TokenInfo;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.repository.RefreshTokenRepository;
import com.gongyeon.gongyeon.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class MemberServiceTest extends GongyeonBaseTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    //signUp
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

    //login
    @Test
    public void 로그인() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        memberService.signUp(member);
        //when
        TokenInfo tokenInfo = memberService.login(member.getEmail(), member.getPassword());
        //then
        String email = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken()).getName();
        Assertions.assertThat(email).isEqualTo(member.getEmail());
    }

    @Test
    public void 로그인_시_이메일_없음() throws Exception {
        Assertions.assertThatThrownBy(() -> {
            memberService.login("test1@gmail.com", "test1");
        });
    }

    //reissue
    @Test
    public void reissue() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        TokenInfo tokenInfo = memberService.signUp(member);
        //when
        TokenInfo newTokenInfo = memberService.reissue(tokenInfo);
        Authentication authentication = jwtTokenProvider.getAuthentication(newTokenInfo.getAccessToken());
        //then
        Assertions.assertThat(refreshTokenRepository.findById(authentication.getName())).isPresent();
        Assertions.assertThat(newTokenInfo.getRefreshToken()).isNotEqualTo(tokenInfo.getRefreshToken());
        Assertions.assertThat(jwtTokenProvider.getAuthentication(newTokenInfo.getAccessToken()).getName())
                .isEqualTo(member.getEmail());
    }

    @Test
    public void reissue_시_잘못된_access_token() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        TokenInfo tokenInfo = memberService.signUp(member);
        //when
        tokenInfo.setAccessToken("wrong_access_token");
        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.reissue(tokenInfo);
        });
    }

    @Test
    public void reissue_시_잘못된_refresh_token() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        TokenInfo tokenInfo = memberService.signUp(member);
        //when
        tokenInfo.setRefreshToken("wrong_refresh_token");
        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.reissue(tokenInfo);
        });
    }

    //logout
    @Test
    public void logout() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        TokenInfo tokenInfo = memberService.signUp(member);
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //when
        memberService.logout();
        //then
        Assertions.assertThat(refreshTokenRepository.findById(member.getEmail())).isNotPresent();
    }

    @Test
    public void logout_시_redis에_refresh_token_없음() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        TokenInfo tokenInfo = memberService.signUp(member);
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //when
        RefreshToken refreshToken = new RefreshToken(member.getEmail(), tokenInfo.getRefreshToken());
        refreshTokenRepository.delete(refreshToken);
        //then
        Assertions.assertThatThrownBy(() -> memberService.logout());
    }
}
