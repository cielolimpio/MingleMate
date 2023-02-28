package com.minglemate.minglemate.models;

import com.minglemate.minglemate.domain.MemberCategory;
import com.minglemate.minglemate.domain.Category;
import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.enums.ParentCategoryEnum;
import com.minglemate.minglemate.enums.PeriodOptionEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryDto {
    private ParentCategoryEnum parentCategory;
    private String name;
    private String description;
    private PeriodOptionEnum periodOption;

    @QueryProjection
    public CategoryDto(MemberCategory membercategory) {
        Category category = membercategory.getCategory();
        this.parentCategory = category.getParentCategory();
        this.name = category.getName();
        this.description = membercategory.getDescription();
        this.periodOption = membercategory.getPeriodOption();
    }
}
