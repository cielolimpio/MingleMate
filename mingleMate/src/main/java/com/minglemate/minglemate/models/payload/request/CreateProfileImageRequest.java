package com.minglemate.minglemate.models.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreateProfileImageRequest {
    @NotNull
    private int index;
    @NotNull
    private String filename;
    @NotNull
    private String originalS3Path;
    @NotNull
    private String thumbnailS3Path;
}
