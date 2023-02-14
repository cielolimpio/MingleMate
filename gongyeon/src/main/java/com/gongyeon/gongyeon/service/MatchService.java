package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.domain.Match;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.enums.MatchingStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import com.gongyeon.gongyeon.models.MatchDto;
import com.gongyeon.gongyeon.provider.AuthenticationProvider;
import com.gongyeon.gongyeon.repository.MatchRepository;
import com.gongyeon.gongyeon.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
        Match match = Match.createMatch(requester, responder);
        matchRepository.save(match);
        return new MatchDto(requester.getName(), responder.getName(), match.getMatchingStatus(), match.getRegisteredDateTime());
    }

    @Transactional
    public MatchDto matchComplete(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.NOT_FOUND, "해당 매치 정보가 존재하지 않습니다."));
        match.changeMatchStatus(MatchingStatusEnum.ACCEPT);
        return new MatchDto(match.getRequester().getName(), match.getResponder().getName(), match.getMatchingStatus(), match.getLastModifiedDateTime());
    }

    @Transactional
    public MatchDto matchReject(Long matchId){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.NOT_FOUND, "해당 매치 정보가 존재하지 않습니다."));
        match.changeMatchStatus(MatchingStatusEnum.REJECT);
        return new MatchDto(match.getRequester().getName(), match.getResponder().getName(), match.getMatchingStatus(), match.getLastModifiedDateTime());
    }
}
