package com.gongyeon.gongyeon.exception;

import com.gongyeon.gongyeon.enums.HttpStatusEnum;

public class GongYeonException extends RuntimeException {

    private HttpStatusEnum httpStatusEnum;

    public GongYeonException(HttpStatusEnum httpStatusEnum, String message) {
        super(message);
        this.httpStatusEnum = httpStatusEnum;
    }
    public GongYeonException(String message) {
        super(message);
        this.httpStatusEnum = HttpStatusEnum.BAD_REQUEST;
    }
}
