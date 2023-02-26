package com.mingleMate.mingleMate.models;

import com.mingleMate.mingleMate.enums.MatchingStatusEnum;
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

    private MatchingStatusEnum matchingStatus;
    private LocalDateTime matchedDate;
}
