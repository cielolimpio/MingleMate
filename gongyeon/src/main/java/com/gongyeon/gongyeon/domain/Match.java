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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Member requester;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "responder_id", nullable = false)
    private Member responder;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MatchingStatusEnum matchingStatus;


    public static Match createMatch(Member requester, Member responder) {
        Match match = new Match();
        match.requester = requester;
        match.responder = responder;
        match.matchingStatus = MatchingStatusEnum.REQUEST;

        return match;
    }

    public void changeMatchStatus(MatchingStatusEnum matchingStatus){
        this.matchingStatus = matchingStatus;
    }
}
