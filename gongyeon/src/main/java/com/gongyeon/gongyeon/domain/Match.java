package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.MatchingStatusEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "matches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "responder_id")
    private Member responder;

    @Enumerated(EnumType.STRING)
    private MatchingStatusEnum matchingStatus;


    public static Match createMatch(Member requester, Member responder) {
        Match match = new Match();
        match.requester = requester;
        match.responder = responder;
        match.matchingStatus = MatchingStatusEnum.REQUEST;

        return match;
    }
}
