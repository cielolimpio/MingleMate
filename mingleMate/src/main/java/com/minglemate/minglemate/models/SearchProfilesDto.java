package com.minglemate.minglemate.models;

import com.minglemate.minglemate.domain.Category;
import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.enums.PeriodOptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.util.Pair;


@Data
@AllArgsConstructor
public class SearchProfilesDto {
    private GenderEnum gender;
    private Pair<Integer, Integer> ageRange;
    private Address address;
    private Category category;
    private PeriodOptionEnum periodOption;
}
