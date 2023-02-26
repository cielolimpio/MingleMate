package com.minglemate.minglemate.repository;

import com.minglemate.minglemate.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
