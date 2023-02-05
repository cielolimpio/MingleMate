package com.gongyeon.gongyeon.domain.embeddedTypes;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class DaysOfTheWeek {
    private boolean mon = false;
    private boolean tue = false;
    private boolean wed = false;
    private boolean thu = false;
    private boolean fri = false;
    private boolean sat = false;
    private boolean sun = false;

    public DaysOfTheWeek() {}

    public DaysOfTheWeek(boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public int countMatchingDaysOfTheWeek(DaysOfTheWeek daysOfTheWeek) {
        int count = 0;
        if (this.isMon() && daysOfTheWeek.isMon()) count++;
        if (this.isTue() && daysOfTheWeek.isTue()) count++;
        if (this.isWed() && daysOfTheWeek.isWed()) count++;
        if (this.isThu() && daysOfTheWeek.isThu()) count++;
        if (this.isFri() && daysOfTheWeek.isFri()) count++;
        if (this.isSat() && daysOfTheWeek.isSat()) count++;
        if (this.isSun() && daysOfTheWeek.isSun()) count++;

        return count;
    }
}
