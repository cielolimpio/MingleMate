package com.mingleMate.mingleMate.service;

import com.mingleMate.mingleMate.domain.Match;
import com.mingleMate.mingleMate.domain.Member;
import com.mingleMate.mingleMate.enums.HttpStatusEnum;
import com.mingleMate.mingleMate.enums.MatchingStatusEnum;
import com.mingleMate.mingleMate.exception.MingleMateException;
import com.mingleMate.mingleMate.models.MatchDto;
import com.mingleMate.mingleMate.provider.AuthenticationProvider;
import com.mingleMate.mingleMate.repository.MatchRepository;
import com.mingleMate.mingleMate.repository.MemberRepository;
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
        return new MatchDto(requester.getName(), responder.getName(), match.getMatchingStatus(), match.getRegisteredDateTime());
    }

    @Transactional
    public MatchDto matchComplete(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "해당 매치 정보가 존재하지 않습니다."));
        match.changeMatchStatus(MatchingStatusEnum.ACCEPT);
        return new MatchDto(match.getRequester().getName(), match.getResponder().getName(), match.getMatchingStatus(), match.getLastModifiedDateTime());
    }

    @Transactional
    public MatchDto matchReject(Long matchId){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "해당 매치 정보가 존재하지 않습니다."));
        match.changeMatchStatus(MatchingStatusEnum.REJECT);
        return new MatchDto(match.getRequester().getName(), match.getResponder().getName(), match.getMatchingStatus(), match.getLastModifiedDateTime());
    }
}
