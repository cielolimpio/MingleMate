package com.gongyeon.gongyeon.payload.response;

import lombok.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity<T> extends HttpEntity<T> {
    private HttpStatus status;
    private T body;

    public ResponseEntity(@Nullable T body, HttpStatus status){
        this.body = body;
        this.status = status;
    }
}
