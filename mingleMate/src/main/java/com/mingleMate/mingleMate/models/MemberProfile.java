package com.mingleMate.mingleMate.models;

import com.mingleMate.mingleMate.domain.MemberStudyField;
import com.mingleMate.mingleMate.domain.embeddedTypes.Address;
import com.mingleMate.mingleMate.domain.embeddedTypes.DaysOfTheWeek;
import com.mingleMate.mingleMate.domain.embeddedTypes.Tags;
import com.mingleMate.mingleMate.enums.GenderEnum;
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
    private List<MemberStudyField> memberStudyFields;
    private Tags tags;

    @QueryProjection
    public MemberProfile(
            Long id,
            String name,
            GenderEnum gender,
            int age,
            Address address,
            DaysOfTheWeek possibleDaysOfTheWeek,
            List<MemberStudyField> memberStudyFields,
            Tags tags
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.possibleDaysOfTheWeek = possibleDaysOfTheWeek;
        this.memberStudyFields = memberStudyFields;
        this.tags = tags;
    }
}