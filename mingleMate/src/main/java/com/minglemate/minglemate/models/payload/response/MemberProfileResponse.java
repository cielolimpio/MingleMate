package com.minglemate.minglemate.models.payload.response;

import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.models.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileResponse {
    private Long id;
    private String name;
    private GenderEnum gender;
    private int age;
    private Address address;
    private List<ProfileImageResponse> profileImages;
    private List<CategoryDto> categories;
}
