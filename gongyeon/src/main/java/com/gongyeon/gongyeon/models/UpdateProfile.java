package com.gongyeon.gongyeon.models;

import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.DaysOfTheWeek;
import com.gongyeon.gongyeon.domain.embeddedTypes.Tags;
import com.gongyeon.gongyeon.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfile {
    private String name;
    private GenderEnum gender;
    private int age;
    private Address address;
    private DaysOfTheWeek possibleDaysOfTheWeek;
    private List<StudyFieldDto> studyFields;
    private Tags tags;
}
