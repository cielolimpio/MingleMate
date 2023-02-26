package com.minglemate.minglemate.models.payload.response;

import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.models.CategoryDto;
import com.minglemate.minglemate.models.ProfileImageDto;
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
    private List<ProfileImageDto> profileImages;
    private List<CategoryDto> studyFields;
}
