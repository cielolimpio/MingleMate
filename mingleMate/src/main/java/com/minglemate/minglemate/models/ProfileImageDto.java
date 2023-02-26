package com.minglemate.minglemate.models;

import com.minglemate.minglemate.domain.ProfileImage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageDto {
    private int index;
    private String originalS3Path;
    private String thumbnailS3Path;

    @QueryProjection
    public ProfileImageDto(ProfileImage profileImage) {
        ProfileImageDto profileImageDto = new ProfileImageDto();
        profileImageDto.index = profileImage.getIndex();
        profileImageDto.originalS3Path = profileImage.getOriginalS3Path();
        profileImageDto.thumbnailS3Path = profileImage.getThumbnailS3Path();
    }
}
