package com.gongyeon.gongyeon.models;

import com.gongyeon.gongyeon.domain.MemberStudyField;
import com.gongyeon.gongyeon.domain.StudyField;
import com.gongyeon.gongyeon.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyFieldDto {
    private CategoryEnum category;
    private String studyFieldName;

    public StudyFieldDto(MemberStudyField memberStudyField) {
        StudyField studyField = memberStudyField.getStudyField();
        this.category = studyField.getCategory();
        if (studyField.getMainName().equals("기타")) {
            this.studyFieldName = memberStudyField.getName();
        }
        else this.studyFieldName = studyField.getMainName();
    }
}
