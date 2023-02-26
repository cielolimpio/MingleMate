package com.mingleMate.mingleMate.exception;

import com.mingleMate.mingleMate.enums.HttpStatusEnum;
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
