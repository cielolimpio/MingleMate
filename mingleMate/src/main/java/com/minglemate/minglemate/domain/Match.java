package com.minglemate.minglemate.domain;

import com.minglemate.minglemate.enums.MatchStatusEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "matches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String uuid;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Member requester;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "responder_id", nullable = false)
    private Member responder;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MatchStatusEnum status;

    @Column(name = "deleted_datetime")
    private LocalDateTime deletedDateTime;


    public static Match createMatch(Member requester, Member responder) {
        Match match = new Match();
        match.uuid = UUID.randomUUID().toString();
        match.requester = requester;
        match.responder = responder;
        match.status = MatchStatusEnum.REQUEST;

        return match;
    }

    public void changeMatchStatus(MatchStatusEnum status){
        this.status = status;
    }
}
