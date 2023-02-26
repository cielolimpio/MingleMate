package com.minglemate.minglemate.models.payload.response;

import lombok.*;
import org.springframework.http.HttpEntity;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor
public class ResponseEntity<T> extends HttpEntity<T> {
    private T body;

    public ResponseEntity(@Nullable T body){
        this.body = body;
    }

}
