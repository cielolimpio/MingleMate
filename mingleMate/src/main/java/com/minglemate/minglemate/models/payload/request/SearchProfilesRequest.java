package com.minglemate.minglemate.models.payload.request;

import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.enums.PeriodOptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.util.Pair;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SearchProfilesRequest {
    private GenderEnum gender;
    private Pair<Integer, Integer> ageRange;
    private Address address;
    @NotNull
    private Long categoryId;
    @NotNull
    private PeriodOptionEnum periodOption;
}