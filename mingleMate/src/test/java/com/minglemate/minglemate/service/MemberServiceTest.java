package com.minglemate.minglemate.service;

import com.minglemate.minglemate.MingleMateBaseTest;
import com.minglemate.minglemate.domain.Category;
import com.minglemate.minglemate.domain.Member;
import com.minglemate.minglemate.domain.MemberCategory;
import com.minglemate.minglemate.domain.RefreshToken;
import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.models.*;
import com.minglemate.minglemate.models.payload.request.SearchProfilesRequest;
import com.minglemate.minglemate.models.payload.request.UpdateProfileRequest;
import com.minglemate.minglemate.models.payload.request.UpdateCategoryRequest;
import com.minglemate.minglemate.models.payload.response.MyPageResponse;
import com.minglemate.minglemate.provider.AuthenticationProvider;
import com.minglemate.minglemate.repository.MemberRepository;
import com.minglemate.minglemate.repository.RefreshTokenRepository;
import com.minglemate.minglemate.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest extends MingleMateBaseTest {

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
        List<Category> baseStudyFields = createBaseStudyFields();

        Member newMember = Member.createMember("test1", "test1@naver.com", "test1");
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login("test1@naver.com", "test1");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Member member = memberRepository.findByEmail(newMember.getEmail()).get();

        UpdateProfileRequest updateProfileMember = new UpdateProfileRequest(
                member.getName(),
                GenderEnum.MALE, 20,
                new Address("부산", "사하구", "다대동"),
                new DaysOfTheWeek(true, true, true, false, false, false, true),
                List.of(
                        new UpdateCategoryRequest(baseStudyFields.get(0).getId(), baseStudyFields.get(0).getMainName()),
                        new UpdateCategoryRequest(baseStudyFields.get(1).getId(), baseStudyFields.get(1).getMainName())
                )
        );


        //when
        memberService.updateProfile(updateProfileMember);

        //then
        assertThat(member.getName()).isEqualTo("test1");
        assertThat(member.getAge()).isEqualTo(20);
        assertThat(member.getAddress().getCity()).isEqualTo("부산");
        assertThat(member.getGender()).isEqualTo(GenderEnum.MALE);
        assertThat(member.getPossibleDaysOfTheWeek().isSun()).isTrue();
        assertThat(member.getPossibleDaysOfTheWeek().isFri()).isFalse();
        assertThat(member.getMemberCategories().size()).isEqualTo(2);
    }

    @Test
    public void 프로필_수정(){
        //given
        List<Category> baseStudyFields = createBaseStudyFields();
        Member newMember = Member.createMember("test1", "test1@naver.com", "test1");
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login("test1@naver.com", "test1");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UpdateProfileRequest profile = new UpdateProfileRequest(
                newMember.getName(),
                GenderEnum.MALE,
                20,
                new Address("부산", "사하구", "다대동"),
                new DaysOfTheWeek(true, true, true, false, false, false, true),
                List.of(new UpdateCategoryRequest(baseStudyFields.get(0).getId(), baseStudyFields.get(0).getMainName()),
                        new UpdateCategoryRequest(baseStudyFields.get(1).getId(), baseStudyFields.get(1).getMainName()))
        );

        UpdateProfileRequest updateProfile = new UpdateProfileRequest(
                "새로운 이름",
                GenderEnum.FEMALE,
                30,
                new Address("서울", "관악구", "봉천동"),
                new DaysOfTheWeek(false, false, false, true, true, true, false),
                List.of(new UpdateCategoryRequest(baseStudyFields.get(3).getId(), baseStudyFields.get(3).getMainName()))
        );

        //when
        memberService.updateProfile(updateProfile);
        Member member = memberRepository.findByEmail(newMember.getEmail())
                .orElseThrow();

        //then
        assertThat(member.getName()).isEqualTo("새로운 이름");
        assertThat(member.getGender()).isEqualTo(GenderEnum.FEMALE);
        assertThat(member.getAddress().getTown()).isEqualTo("관악구");
        assertThat(member.getPossibleDaysOfTheWeek().isMon()).isFalse();
        assertThat(member.getPossibleDaysOfTheWeek().isSat()).isTrue();
        assertThat(member.getMemberCategories().size()).isEqualTo(1);
    }


    @Test
    public void 마이페이지(){
        //given
        List<Category> baseStudyFields = createBaseStudyFields();
        Member newMember = Member.createMember("test1", "test1@naver.com", "test1");
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login("test1@naver.com", "test1");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Member member = memberRepository.findByEmail(newMember.getEmail())
                .orElseThrow();

        UpdateProfileRequest updateProfileMember = new UpdateProfileRequest(
                member.getName(),
                GenderEnum.MALE,
                20,
                new Address("부산", "사하구", "다대동"),
                new DaysOfTheWeek(true, true, true, false, false, false, true),
                List.of(new UpdateCategoryRequest(baseStudyFields.get(0).getId(), baseStudyFields.get(0).getMainName()),
                        new UpdateCategoryRequest(baseStudyFields.get(1).getId(), baseStudyFields.get(1).getMainName()))
        );
        memberService.updateProfile(updateProfileMember);

        //when
        MyPageResponse myPageResult = memberService.myPage();

        //then
        assertThat(myPageResult.getName()).isEqualTo("test1");
        assertThat(myPageResult.getEmail()).isEqualTo("test1@naver.com");
        assertThat(myPageResult.getAddress().getVillage()).isEqualTo("다대동");
        assertThat(myPageResult.getGender()).isEqualTo(GenderEnum.MALE);
        assertThat(myPageResult.getStudyFields().size()).isEqualTo(2);
        assertThat(myPageResult.getPossibleDaysOfTheWeek().isWed()).isTrue();
    }

    @Test
    public void searchProfiles() throws Exception {
        //given
        List<Category> baseStudyFields = createBaseStudyFields();
        createBaseMembers(baseStudyFields);
        Member newMember = Member.createMember("test1", "test1@naver.com", "test1");
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login("test1@naver.com", "test1");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UpdateProfileRequest updateProfileMember = new UpdateProfileRequest(
                newMember.getName(),
                GenderEnum.MALE,
                25,
                new Address("city1", "town1", "village1"),
                new DaysOfTheWeek(true, true, true, false, false, false, true),
                List.of(new UpdateCategoryRequest(baseStudyFields.get(0).getId(), baseStudyFields.get(0).getMainName()),
                        new UpdateCategoryRequest(baseStudyFields.get(8).getId(), baseStudyFields.get(8).getMainName()))
        );
        memberService.updateProfile(updateProfileMember);
        Member member = AuthenticationProvider.getCurrentMember();
        //when
        List<Long> memberStudyFieldIds = member.getMemberCategories().stream().map(MemberCategory::getId).collect(Collectors.toList());
        Page<MemberProfile> memberProfiles = memberService.searchProfiles(
                new SearchProfilesRequest(
                        GenderEnum.FEMALE,
                        Pair.of(23, 27),
                        new Address("city1", "town1", null),
                        new DaysOfTheWeek(true, true, true, false, false, false, true),
                        memberStudyFieldIds
                ),
                Pageable.ofSize(10)
        );

        //then
        memberProfiles.forEach(memberProfile -> {
            Assertions.assertThat(memberProfile.getGender()).isEqualTo(GenderEnum.FEMALE);
            Assertions.assertThat(memberProfile.getAge()).isGreaterThanOrEqualTo(23);
            Assertions.assertThat(memberProfile.getAge()).isLessThanOrEqualTo(27);
            Assertions.assertThat(memberProfile.getAddress().getCity()).isEqualTo("city1");
            Assertions.assertThat(memberProfile.getAddress().getTown()).isEqualTo("town1");
            int numOfMatchingDaysOfTheWeek = memberProfile.getPossibleDaysOfTheWeek()
                    .countMatchingDaysOfTheWeek(member.getPossibleDaysOfTheWeek());
            Assertions.assertThat(numOfMatchingDaysOfTheWeek).isPositive();
        });
    }

}
