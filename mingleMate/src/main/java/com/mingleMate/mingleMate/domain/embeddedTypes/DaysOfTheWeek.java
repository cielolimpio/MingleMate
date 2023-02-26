package com.mingleMate.mingleMate.domain.embeddedTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DaysOfTheWeek {
    @Column(nullable = false)
    private boolean mon = false;
    @Column(nullable = false)
    private boolean tue = false;
    @Column(nullable = false)
    private boolean wed = false;
    @Column(nullable = false)
    private boolean thu = false;
    @Column(nullable = false)
    private boolean fri = false;
    @Column(nullable = false)
    private boolean sat = false;
    @Column(nullable = false)
    private boolean sun = false;


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
