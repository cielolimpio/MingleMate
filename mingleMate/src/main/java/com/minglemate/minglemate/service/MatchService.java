package com.minglemate.minglemate.service;

import com.minglemate.minglemate.domain.Match;
import com.minglemate.minglemate.domain.Member;
import com.minglemate.minglemate.enums.HttpStatusEnum;
import com.minglemate.minglemate.enums.MatchStatusEnum;
import com.minglemate.minglemate.exception.MingleMateException;
import com.minglemate.minglemate.models.MatchDto;
import com.minglemate.minglemate.provider.AuthenticationProvider;
import com.minglemate.minglemate.repository.MatchRepository;
import com.minglemate.minglemate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final MatchRepository matchRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public MatchDto matchRequest(Long responderId) {
        Member requester = AuthenticationProvider.getCurrentMember();
        Member responder = memberRepository.findById(responderId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
        Match match = Match.createMatch(requester, responder);
        matchRepository.save(match);
        return new MatchDto(requester.getName(), responder.getName(), match.getStatus(), match.getRegisteredDateTime());
    }

    @Transactional
    public MatchDto changeMatchStatus(Long matchId, MatchStatusEnum matchStatus) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "해당 매치 정보가 존재하지 않습니다."));
        match.changeMatchStatus(MatchStatusEnum.ACCEPT);
        return new MatchDto(match.getRequester().getName(), match.getResponder().getName(), match.getStatus(), match.getLastModifiedDateTime());
    }
}
