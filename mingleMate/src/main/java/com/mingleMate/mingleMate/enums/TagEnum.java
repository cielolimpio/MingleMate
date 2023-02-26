package com.mingleMate.mingleMate.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagEnum {
    ATTENDANCE("ATTENDANCE"),
    KINDNESS("KINDNESS"),
    STUDYING_HARD("STUDYING_HARD")
    ;

    @JsonValue
    private final String tag;
}
