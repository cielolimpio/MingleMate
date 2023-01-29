package com.gongyeon.gongyeon.repository;

import com.gongyeon.gongyeon.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
