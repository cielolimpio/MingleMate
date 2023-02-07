package com.gongyeon.gongyeon.models;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.enums.MatchingStatusEnum;
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
