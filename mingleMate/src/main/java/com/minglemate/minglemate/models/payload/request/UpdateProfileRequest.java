package com.minglemate.minglemate.models.payload.request;

import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
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
    private List<UpdateCategoryRequest> categories;
}