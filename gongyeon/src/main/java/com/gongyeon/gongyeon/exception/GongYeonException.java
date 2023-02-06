package com.gongyeon.gongyeon.exception;

import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import lombok.Getter;

@Getter
public class GongYeonException extends RuntimeException {

    private HttpStatusEnum httpStatusEnum;
    private String message;

    public GongYeonException(HttpStatusEnum httpStatusEnum, String message) {
        this.message = message;
        this.httpStatusEnum = httpStatusEnum;
    }
    public GongYeonException(String message) {
        this.message = message;
        this.httpStatusEnum = HttpStatusEnum.BAD_REQUEST;
    }
}
