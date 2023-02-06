package com.gongyeon.gongyeon.exception;

import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResult {
    private HttpStatusEnum httpStatusEnum;
    private String message;

    public ErrorResult(String message) {
        this.message = message;
    }
}
