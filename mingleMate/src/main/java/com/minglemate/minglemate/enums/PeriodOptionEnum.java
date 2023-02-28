package com.minglemate.minglemate.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PeriodOptionEnum {
    PERIODICALLY("PERIODICALLY"),
    FREELY("FREELY"),
    NO_MATTER("NO_MATTER")
    ;

    @JsonValue
    private final String option;
}
