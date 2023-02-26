package com.minglemate.minglemate.repository;

import com.minglemate.minglemate.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
