package com.minglemate.minglemate.models.payload.request;

import com.minglemate.minglemate.enums.PeriodOptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateCategoryRequest {
    @NotNull
    private Long categoryId;

    private String description;

    @NotNull
    private PeriodOptionEnum periodOption;
}
