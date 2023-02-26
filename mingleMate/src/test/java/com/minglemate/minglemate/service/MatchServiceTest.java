package com.minglemate.minglemate.service;

import com.minglemate.minglemate.MingleMateBaseTest;
import com.minglemate.minglemate.domain.Match;
import com.minglemate.minglemate.domain.Member;
import com.minglemate.minglemate.enums.MatchStatusEnum;
import com.minglemate.minglemate.models.MatchDto;
import com.minglemate.minglemate.repository.MemberRepository;
import com.minglemate.minglemate.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

class MatchServiceTest extends MingleMateBaseTest {

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
        assertThat(matchDto.getMatchingStatus()).isEqualTo(MatchStatusEnum.REQUEST);
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
        MatchDto successMatch = matchService.changeMatchStatus(match.getId());

        //then
        assertThat(successMatch.getMatchingStatus()).isEqualTo(MatchStatusEnum.ACCEPT);
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
        assertThat(failMatch.getMatchingStatus()).isEqualTo(MatchStatusEnum.REJECT);
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