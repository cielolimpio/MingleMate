package com.gongyeon.gongyeon.models;

import com.gongyeon.gongyeon.domain.StudyField;
import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.Days;
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
    private Days possibleDays;
    private List<StudyField> studyFields;
    private Tags tags;

    @QueryProjection
    public MemberProfile(
            Long id,
            String name,
            GenderEnum gender,
            int age,
            Address address,
            Days possibleDays,
            List<StudyField> studyFields,
            Tags tags
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.possibleDays = possibleDays;
        this.studyFields = studyFields;
        this.tags = tags;
    }
}
