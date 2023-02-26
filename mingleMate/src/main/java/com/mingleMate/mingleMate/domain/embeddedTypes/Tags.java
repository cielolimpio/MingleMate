package com.mingleMate.mingleMate.domain.embeddedTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tags {
    @Column(nullable = false)
    private Integer attendance = 0;
    @Column(nullable = false)
    private Integer kindness = 0;
    @Column(nullable = false)
    private Integer studyingHard = 0;
}
