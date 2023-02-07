package com.gongyeon.gongyeon.models;

import com.gongyeon.gongyeon.domain.StudyField;
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
public class MyPageDto {
    private String name;
    private String email;
    private GenderEnum gender;
    private int age;
    private Address address;
    private DaysOfTheWeek possibleDaysOfTheWeek;
    private List<StudyField> studyFields;
    private Tags tags;
}
