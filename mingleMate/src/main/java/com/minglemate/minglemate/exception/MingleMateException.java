package com.minglemate.minglemate.exception;

import com.minglemate.minglemate.enums.HttpStatusEnum;
import lombok.Getter;

@Getter
public class MingleMateException extends RuntimeException {

    private HttpStatusEnum httpStatusEnum;
    private String message;

    public MingleMateException(HttpStatusEnum httpStatusEnum, String message) {
        this.message = message;
        this.httpStatusEnum = httpStatusEnum;
    }
    public MingleMateException(String message) {
        this.message = message;
        this.httpStatusEnum = HttpStatusEnum.BAD_REQUEST;
    }
}
