package com.minglemate.minglemate.models.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProfileImageRequest {
    private Long profileImageId;
    private int index;
}
