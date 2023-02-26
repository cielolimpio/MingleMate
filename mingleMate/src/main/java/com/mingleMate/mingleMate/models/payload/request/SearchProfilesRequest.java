package com.mingleMate.mingleMate.models.payload.request;

import com.mingleMate.mingleMate.domain.embeddedTypes.Address;
import com.mingleMate.mingleMate.domain.embeddedTypes.DaysOfTheWeek;
import com.mingleMate.mingleMate.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.util.Pair;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchProfilesRequest {
    private GenderEnum gender;
    private Pair<Integer, Integer> ageRange;
    private Address address;
    @NotNull
    private DaysOfTheWeek daysOfTheWeek;
    private List<Long> memberStudyFieldIds;
}