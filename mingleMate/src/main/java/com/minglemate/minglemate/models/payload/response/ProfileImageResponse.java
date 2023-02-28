package com.minglemate.minglemate.models.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageResponse {
    private int index;
    private String imageUrl;
}
