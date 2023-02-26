package com.minglemate.minglemate.domain;

import com.minglemate.minglemate.enums.PeriodOptionEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "member_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCategory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PeriodOptionEnum periodOption;

    @Column(name = "deleted_datetime")
    private LocalDateTime deletedDateTime;


    public static MemberCategory createMemberCategory(Member member, Category category, String description, PeriodOptionEnum periodOption) {
        MemberCategory memberCategory = new MemberCategory();
        memberCategory.member = member;
        memberCategory.category = category;
        memberCategory.description = description;
        memberCategory.periodOption = periodOption;

        return memberCategory;
    }
}
