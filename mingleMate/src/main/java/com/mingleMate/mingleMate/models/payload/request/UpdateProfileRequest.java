package com.mingleMate.mingleMate.models.payload.request;

import com.mingleMate.mingleMate.domain.embeddedTypes.Address;
import com.mingleMate.mingleMate.domain.embeddedTypes.DaysOfTheWeek;
import com.mingleMate.mingleMate.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateProfileRequest {
    private String name;
    private GenderEnum gender;
    private int age;
    private Address address;
    private DaysOfTheWeek possibleDaysOfTheWeek;
    private List<UpdateStudyFieldRequest> studyFields;
}