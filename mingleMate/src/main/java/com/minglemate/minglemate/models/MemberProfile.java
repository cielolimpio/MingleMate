package com.minglemate.minglemate.models;

import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
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
    private List<ProfileImageDto> profileImages;
    private List<CategoryDto> categories;

    @QueryProjection
    public MemberProfile(
            Long id,
            String name,
            GenderEnum gender,
            int age,
            Address address,
            List<ProfileImageDto> profileImages,
            List<CategoryDto> categories
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.profileImages = profileImages;
        this.categories = categories;
    }
}