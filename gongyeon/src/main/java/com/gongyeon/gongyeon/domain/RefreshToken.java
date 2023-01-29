package com.gongyeon.gongyeon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 3600)
@Getter
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String memberEmail;
    private String refreshToken;
}
