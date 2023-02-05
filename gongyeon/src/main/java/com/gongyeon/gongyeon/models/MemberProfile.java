package com.gongyeon.gongyeon.models;

import com.gongyeon.gongyeon.domain.StudyField;
import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.DaysOfTheWeek;
import com.gongyeon.gongyeon.domain.embeddedTypes.Tags;
import com.gongyeon.gongyeon.enums.GenderEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberProfile {
    private Long id;
    private String name;
    private GenderEnum gender;
    private int age;
    private Address address;
    private DaysOfTheWeek possibleDaysOfTheWeek;
    private List<StudyField> studyFields;
    private Tags tags;

    @QueryProjection
    public MemberProfile(
            Long id,
            String name,
            GenderEnum gender,
            int age,
            Address address,
            DaysOfTheWeek possibleDaysOfTheWeek,
            List<StudyField> studyFields,
            Tags tags
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.possibleDaysOfTheWeek = possibleDaysOfTheWeek;
        this.studyFields = studyFields;
        this.tags = tags;
    }
}