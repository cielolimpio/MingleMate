package com.mingleMate.mingleMate.repository;

import com.mingleMate.mingleMate.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
