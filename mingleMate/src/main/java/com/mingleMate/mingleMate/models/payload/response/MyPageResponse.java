package com.mingleMate.mingleMate.models.payload.response;

import com.mingleMate.mingleMate.domain.embeddedTypes.Address;
import com.mingleMate.mingleMate.domain.embeddedTypes.DaysOfTheWeek;
import com.mingleMate.mingleMate.domain.embeddedTypes.Tags;
import com.mingleMate.mingleMate.enums.GenderEnum;
import com.mingleMate.mingleMate.models.StudyFieldDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageResponse {
    private String name;
    private String email;
    private GenderEnum gender;
    private int age;
    private Address address;
    private DaysOfTheWeek possibleDaysOfTheWeek;
    private List<StudyFieldDto> studyFields;
    private Tags tags;
}
