package com.gongyeon.gongyeon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "matching_reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingReview extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer_id")
    private Member reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "target_member_id")
    private Member targetMember;

    @Nullable
    private Boolean attendance;
    @Nullable
    private Boolean kindness;
    @Nullable
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
