package com.mingleMate.mingleMate.models.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateStudyFieldRequest {
    @NotNull
    private Long studyFieldId;

    //원래 있던 studyField면 해당 studyField의 mainName 넣어주기
    @NotNull
    private String name;
}
