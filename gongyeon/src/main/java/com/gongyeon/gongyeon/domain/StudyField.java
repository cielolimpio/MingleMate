package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.CategoryEnum;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "study_fields")
@Getter
public class StudyField extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;


    public static StudyField createStudyField(Member member, String name, CategoryEnum category) {
        StudyField studyField = new StudyField();
        studyField.member = member;
        studyField.name = name;
        studyField.category = category;

        return studyField;
    }
}
