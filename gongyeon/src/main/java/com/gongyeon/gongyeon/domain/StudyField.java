package com.gongyeon.gongyeon.domain;

import com.gongyeon.gongyeon.enums.CategoryEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "study_fields")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyField extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // if null, it means all of categories
    // if 기타, we have to handle it in admin
    @Column(length = 20, name = "main_name")
    private String mainName;

    @Column(length = 20, name = "sub_name")
    private String subName;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;


    public static StudyField createStudyField(String mainName, String subName, CategoryEnum category) {
        StudyField studyField = new StudyField();
        studyField.mainName = mainName;
        studyField.subName = subName;
        studyField.category = category;

        return studyField;
    }
}
