package com.gongyeon.gongyeon.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryEnum {
    DEFAULT("DEFAULT"),
    개발("개발"),
    어학("어학"),
    취업("취업"),
    고시_공무원("고시_공무원"),
    취미_교양("취미_교양"),
    자율("자율"),

    공대("공대"),
    자연대("자연대"),
    인문대("인문대"),
    사회대("사회대"),
    상경대("상경대"),
    사범대("사범대"),
    예체능("예체능"),
    의대_약대("의대_약대"),
    기타("기타")
    ;

    @JsonValue
    private final String category;
}
