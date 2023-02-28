package com.minglemate.minglemate.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParentCategoryEnum {
    스터디("스터디"),
    스포츠("스포츠"),
    게임("게임"),
    교양("교양"),
    기타("기타"),
    ;

    @JsonValue
    private final String category;
}
