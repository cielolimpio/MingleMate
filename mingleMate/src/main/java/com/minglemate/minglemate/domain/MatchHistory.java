package com.minglemate.minglemate.domain;

import com.minglemate.minglemate.enums.MatchStatusEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "match_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MatchStatusEnum status;


    public static MatchHistory createMatchHistory(Match match, MatchStatusEnum status) {
        MatchHistory matchHistory = new MatchHistory();
        matchHistory.match = match;
        matchHistory.status = status;

        return matchHistory;
    }
}
