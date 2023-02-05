package com.gongyeon.gongyeon.payload.request;

import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.DaysOfTheWeek;
import com.gongyeon.gongyeon.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchProfilesRequest {
    private GenderEnum gender;
    private Pair<Integer, Integer> ageRange;
    private Address address;
    private DaysOfTheWeek daysOfTheWeek;
    private List<String> studyFields;
}