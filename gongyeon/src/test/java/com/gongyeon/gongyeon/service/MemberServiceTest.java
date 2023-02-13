package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.GongyeonBaseTest;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.domain.RefreshToken;
import com.gongyeon.gongyeon.domain.StudyField;
import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.DaysOfTheWeek;
import com.gongyeon.gongyeon.domain.embeddedTypes.Tags;
import com.gongyeon.gongyeon.enums.CategoryEnum;
import com.gongyeon.gongyeon.enums.GenderEnum;
import com.gongyeon.gongyeon.models.*;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.repository.RefreshTokenRepository;
import com.gongyeon.gongyeon.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest extends GongyeonBaseTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PersistenceContext
    EntityManager em;

    //signUp
    @Test
    public void signUp() throws Exception {
        //given
        Member member = Member.createMember("test1", "test1@gmail.com", "test1");
        TokenInfo tokenInfo = memberService.signUp(member);

        //when
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        String email = authentication.getName();
        assertThat(email).isEqualTo("test1@gmail.com");

        //then
        Optional<Member> memberFound = memberRepository.findByEmail(email);
        assertThat(memberFound).isPresent();
        assertThat(memberFound.get().getName()).isEqualTo("test1");
    }

    @Test
    public void 회원가입_이메일_중복() throws Exception {
        //given
        Member member1 = Member.createMember("test1", "test1@gmail.com", "test1");
        memberService.signUp(member1);
        Member member2 = Member.createMember("test2", "test1@gmail.com", "test2");

        //then
        assertThatThrownBy(() -> {
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
        assertThat(email).isEqualTo(member.getEmail());
    }

    @Test
    public void 로그인_시_이메일_없음() throws Exception {
        assertThatThrownBy(() -> {
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
        assertThat(refreshTokenRepository.findById(authentication.getName())).isPresent();
        assertThat(newTokenInfo.getRefreshToken()).isNotEqualTo(tokenInfo.getRefreshToken());
        assertThat(jwtTokenProvider.getAuthentication(newTokenInfo.getAccessToken()).getName())
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
        assertThatThrownBy(() -> {
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
        assertThatThrownBy(() -> {
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
        assertThat(refreshTokenRepository.findById(member.getEmail())).isNotPresent();
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
        assertThatThrownBy(() -> memberService.logout());
    }

    @Test
    public void 프로필_등록(){
        //given
        Member newMember = Member.createMember("test1", "test1@naver.com", "test1");
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login("test1@naver.com", "test1");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Member member = memberRepository.findByEmail(newMember.getEmail()).get();

        UpdateProfile updateProfileMember = new UpdateProfile(
                member.getName(),
                GenderEnum.MALE, 20,
                new Address("부산", "사하구", "다대동"),
                new DaysOfTheWeek(true, true, true, false, false, false, true),
                List.of(new StudyFieldDto("프로그래밍", CategoryEnum.개발), new StudyFieldDto("영어", CategoryEnum.어학)),
                null);


        //when
        memberService.updateProfiles(updateProfileMember);

        //then
        assertThat(member.getName()).isEqualTo("test1");
        assertThat(member.getAge()).isEqualTo(20);
        assertThat(member.getAddress().getCity()).isEqualTo("부산");
        assertThat(member.getGender()).isEqualTo(GenderEnum.MALE);
        assertThat(member.getPossibleDaysOfTheWeek().isSun()).isTrue();
        assertThat(member.getPossibleDaysOfTheWeek().isFri()).isFalse();
        assertThat(member.getStudyFields().size()).isEqualTo(2);
    }

    @Test
    public void 프로필_수정(){
        //given
        Member newMember = Member.createMember("test1", "test1@naver.com", "test1");
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login("test1@naver.com", "test1");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UpdateProfile profile = new UpdateProfile(
                newMember.getName(),
                GenderEnum.MALE,
                20,
                new Address("부산", "사하구", "다대동"),
                new DaysOfTheWeek(true, true, true, false, false, false, true),
                List.of(new StudyFieldDto("프로그래밍", CategoryEnum.개발), new StudyFieldDto("영어", CategoryEnum.어학)),
                null);

        UpdateProfile updateProfile = new UpdateProfile(
                "새로운 이름",
                GenderEnum.FEMALE,
                30,
                new Address("서울", "관악구", "봉천동"),
                new DaysOfTheWeek(false, false, false, true, true, true, false),
                List.of(new StudyFieldDto("수학", CategoryEnum.공대)),
                null
        );

        //when
        memberService.updateProfiles(updateProfile);
        Member member = memberRepository.findByEmail(newMember.getEmail())
                .orElseThrow();

        //then
        assertThat(member.getName()).isEqualTo("새로운 이름");
        assertThat(member.getGender()).isEqualTo(GenderEnum.FEMALE);
        assertThat(member.getAddress().getTown()).isEqualTo("관악구");
        assertThat(member.getPossibleDaysOfTheWeek().isMon()).isFalse();
        assertThat(member.getPossibleDaysOfTheWeek().isSat()).isTrue();
        assertThat(member.getStudyFields().size()).isEqualTo(1);
    }


    @Test
    public void 마이페이지(){
        //given
        Member newMember = Member.createMember("test1", "test1@naver.com", "test1");
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login("test1@naver.com", "test1");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Member member = memberRepository.findByEmail(newMember.getEmail())
                .orElseThrow();

        UpdateProfile updateProfileMember = new UpdateProfile(
                member.getName(),
                GenderEnum.MALE, 20,
                new Address("부산", "사하구", "다대동"),
                new DaysOfTheWeek(true, true, true, false, false, false, true),
                List.of(new StudyFieldDto("프로그래밍", CategoryEnum.개발), new StudyFieldDto("영어", CategoryEnum.어학)),
                null);
        memberService.updateProfiles(updateProfileMember);

        //when
        MyPageDto myPageResult = memberService.myPage();

        //then
        assertThat(myPageResult.getName()).isEqualTo("test1");
        assertThat(myPageResult.getEmail()).isEqualTo("test1@naver.com");
        assertThat(myPageResult.getAddress().getVillage()).isEqualTo("다대동");
        assertThat(myPageResult.getGender()).isEqualTo(GenderEnum.MALE);
        assertThat(myPageResult.getStudyFields().size()).isEqualTo(2);
        assertThat(myPageResult.getPossibleDaysOfTheWeek().isWed()).isTrue();
    }



}
