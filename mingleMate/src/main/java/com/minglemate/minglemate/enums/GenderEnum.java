package com.minglemate.minglemate.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {
    MALE("MALE"), FEMALE("FEMALE");

    @JsonValue
    private final String gender;
}
