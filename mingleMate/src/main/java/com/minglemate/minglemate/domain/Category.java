package com.minglemate.minglemate.domain;

import com.minglemate.minglemate.enums.ParentCategoryEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ParentCategoryEnum parentCategory;

    //null일 때 모두
    @Column(length = 20)
    private String name;


    public static Category createCategory(ParentCategoryEnum parentCategory, String name) {
        Category category = new Category();
        category.parentCategory = parentCategory;
        category.name = name;

        return category;
    }
}
