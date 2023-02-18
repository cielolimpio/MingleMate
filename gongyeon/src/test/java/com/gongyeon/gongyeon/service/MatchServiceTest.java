package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.GongyeonBaseTest;
import com.gongyeon.gongyeon.GongyeonTestHelper;
import com.gongyeon.gongyeon.domain.Match;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.enums.MatchingStatusEnum;
import com.gongyeon.gongyeon.models.MatchDto;
import com.gongyeon.gongyeon.models.TokenInfo;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchServiceTest extends GongyeonBaseTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MatchService matchService;

    @Autowired
    MemberService memberService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 매칭_요청(){
        //given
        Member requester = signUpAndLoginWithAuth("requester", "requester@naver.com", "requester");
        Member responder = signUp("responder", "responder@naver.com", "responder");

        //when
        MatchDto matchDto = matchService.matchRequest(responder.getId());

        //then
        assertThat(matchDto.getMatchingStatus()).isEqualTo(MatchingStatusEnum.REQUEST);
        assertThat(matchDto.getResponderName()).isEqualTo(responder.getName());
        assertThat(matchDto.getRequesterName()).isEqualTo(requester.getName());
    }

    @Test
    public void 매칭_응답_성공(){
        //given
        Member responder = signUpAndLoginWithAuth("respnder", "responder@naver.com", "responder");
        Member requester = signUp("requester", "requester@naver.com", "requester");

        Match match = Match.createMatch(requester, responder);
        em.persist(match);

        //when
        MatchDto successMatch = matchService.matchComplete(match.getId());

        //then
        assertThat(successMatch.getMatchingStatus()).isEqualTo(MatchingStatusEnum.ACCEPT);
        assertThat(successMatch.getResponderName()).isEqualTo(responder.getName());
        assertThat(successMatch.getRequesterName()).isEqualTo(requester.getName());

    }

    @Test
    public void 매칭_응답_실패(){
        //given
        Member responder = signUpAndLoginWithAuth("respnder", "responder@naver.com", "responder");
        Member requester = signUp("requester", "requester@naver.com", "requester");

        Match match = Match.createMatch(requester, responder);
        em.persist(match);

        //when
        MatchDto failMatch = matchService.matchReject(match.getId());

        //then
        assertThat(failMatch.getMatchingStatus()).isEqualTo(MatchingStatusEnum.REJECT);
        assertThat(failMatch.getResponderName()).isEqualTo(responder.getName());
        assertThat(failMatch.getRequesterName()).isEqualTo(requester.getName());
    }

//    private Member signUp(String username, String email, String password){
//        Member newMember = Member.createMember(username, email, password);
//        memberService.signUp(newMember);
//        return memberRepository.findByEmail(newMember.getEmail()).get();
//    }
//
//    private Member signUpAndLoginWithAuth(String username, String email, String password) {
//        Member newMember = Member.createMember(username, email, password);
//        memberService.signUp(newMember);
//        TokenInfo tokenInfo = memberService.login(newMember.getEmail(), newMember.getPassword());
//        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return memberRepository.findByEmail(newMember.getEmail()).get();
//    }

}