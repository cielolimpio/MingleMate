package com.gongyeon.gongyeon.models;

import com.gongyeon.gongyeon.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyFieldDto {
    private String studyFieldName;
    private CategoryEnum category;
}
