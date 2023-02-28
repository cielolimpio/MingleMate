package com.minglemate.minglemate.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchStatusEnum {
    REQUEST("REQUEST"),
    REJECT("REJECT"),
    ACCEPT("ACCEPT"),
    PAYMENT_COMPLETE("PAYMENT_COMPLETE"),

    PAYMENT_CANCEL("PAYMENT_CANCEL")
    ;

    @JsonValue
    private final String status;
}
