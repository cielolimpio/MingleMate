package com.mingleMate.mingleMate.models;

import com.mingleMate.mingleMate.domain.MemberStudyField;
import com.mingleMate.mingleMate.domain.StudyField;
import com.mingleMate.mingleMate.enums.CategoryEnum;
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
