package com.gongyeon.gongyeon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "possible_days")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PossibleDay extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Boolean mon = false;
    private Boolean tue = false;
    private Boolean wed = false;
    private Boolean thu = false;
    private Boolean fri = false;
    private Boolean sat = false;
    private Boolean sun = false;


    public static PossibleDay createPossibleDay(Boolean mon, Boolean tue, Boolean wed, Boolean thu, Boolean fri, Boolean sat, Boolean sun) {
        PossibleDay possibleDay = new PossibleDay();
        possibleDay.mon = mon;
        possibleDay.tue = tue;
        possibleDay.wed = wed;
        possibleDay.thu = thu;
        possibleDay.fri = fri;
        possibleDay.sat = sat;
        possibleDay.sun = sun;

        return possibleDay;
    }
}
