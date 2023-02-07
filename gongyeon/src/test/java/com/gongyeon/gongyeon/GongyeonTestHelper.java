package com.gongyeon.gongyeon;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.domain.StudyField;
import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.DaysOfTheWeek;
import com.gongyeon.gongyeon.enums.CategoryEnum;
import com.gongyeon.gongyeon.enums.GenderEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GongyeonTestHelper {

    @PersistenceContext
    EntityManager em;

    public void createBaseMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = createMember(i);
            em.persist(member);
        });
    }

    private Member createMember(int i) {
        Member member = Member.createMember("member"+i, "member"+i+"@gmail.com", "base");
        em.persist(member);

        GenderEnum gender = i % 2 == 1 ? GenderEnum.MALE : GenderEnum.FEMALE;
        int age = i / 8 + 18;
        Address address = new Address(
                i % 2 == 1 ? "city1" : "city2",
                "town" + i % 10,
                "village" + i % 30
        );
        DaysOfTheWeek daysOfTheWeek = new DaysOfTheWeek();
        daysOfTheWeek.setMon(i % 2 == 1);
        daysOfTheWeek.setTue(i % 3 == 1);
        daysOfTheWeek.setWed(i % 4 == 1);
        daysOfTheWeek.setThu(i % 5 == 1);
        daysOfTheWeek.setFri(i % 6 == 1);
        daysOfTheWeek.setSat(i % 7 == 1);
        daysOfTheWeek.setSun(i % 8 == 1);

        CategoryEnum[] categories = CategoryEnum.values();
        List<StudyField> studyFieldList = IntStream.rangeClosed(1, 3).mapToObj(sfi -> {
            Random rand = new Random(System.currentTimeMillis());
            int index = rand.nextInt(categories.length);
            CategoryEnum category = categories[index];
            StudyField studyField = StudyField.createStudyField(member, category.name()+sfi, category);
            em.persist(studyField);
            return studyField;
        }).collect(Collectors.toList());

        member.updateMember(gender, age, address, daysOfTheWeek, studyFieldList);
        return member;
    }
}
