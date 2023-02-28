package com.minglemate.minglemate.repository;

import com.minglemate.minglemate.domain.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
}
