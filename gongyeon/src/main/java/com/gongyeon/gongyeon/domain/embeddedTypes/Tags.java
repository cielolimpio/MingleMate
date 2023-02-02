package com.gongyeon.gongyeon.domain.embeddedTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tags {
    private Integer attendance = 0;
    private Integer kindness = 0;
    private Integer studyingHard = 0;
}
