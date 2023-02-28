package com.minglemate.minglemate.models;

import com.minglemate.minglemate.enums.MatchStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDto {
    private String requesterName;
    private String responderName;

    private MatchStatusEnum matchingStatus;
    private LocalDateTime matchedDate;
}
