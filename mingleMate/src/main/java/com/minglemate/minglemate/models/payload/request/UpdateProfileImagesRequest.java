package com.minglemate.minglemate.models.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateProfileImagesRequest {
    private List<CreateProfileImageRequest> profileImagesToCreate;
    private List<Long> profileImageIdsToDelete;
    private List<UpdateProfileImageRequest> profileImagesToUpdate;

}
