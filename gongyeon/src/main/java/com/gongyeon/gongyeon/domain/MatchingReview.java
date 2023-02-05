package com.gongyeon.gongyeon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "matching_reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingReview extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Member reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "target_member_id", nullable = false)
    private Member targetMember;

    private Boolean attendance;
    private Boolean kindness;
    private Boolean studyingHard;


    public static MatchingReview createMatchingReview(Match match, Member reviewer, Member targetMember, Boolean attendance, Boolean kindness, Boolean studyingHard) {
        MatchingReview matchingReview = new MatchingReview();
        matchingReview.match = match;
        matchingReview.reviewer = reviewer;
        matchingReview.targetMember = targetMember;
        matchingReview.attendance = attendance;
        matchingReview.kindness = kindness;
        matchingReview.studyingHard = studyingHard;

        return matchingReview;
    }
}
