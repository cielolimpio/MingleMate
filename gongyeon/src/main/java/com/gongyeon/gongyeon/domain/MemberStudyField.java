package com.gongyeon.gongyeon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "member_study_fields")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStudyField extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_field_id", nullable = false)
    private StudyField studyField;

    @Column(nullable = false, length = 20)
    private String name;


    public static MemberStudyField createMemberStudyField(Member member, StudyField studyField, String name) {
        MemberStudyField memberStudyField = new MemberStudyField();
        memberStudyField.member = member;
        memberStudyField.studyField = studyField;
        memberStudyField.name = name;

        return memberStudyField;
    }
}
