package com.gongyeon.gongyeon;

import com.gongyeon.gongyeon.domain.Address;
import com.gongyeon.gongyeon.domain.Match;
import com.gongyeon.gongyeon.domain.MatchingReview;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.enums.GenderEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class EntityTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void entityTest() throws Exception {
        //given
        Member member1 = Member.createMember(
                "유민",
                "test1@gmail.com",
                "password1",
                GenderEnum.MALE,
                25,
                new Address("city1", "town1", "village1")
        );
        Member member2 = Member.createMember(
                "동현",
                "test2@gmail.com",
                "password2",
                GenderEnum.MALE,
                25,
                new Address("city2", "town2", "village2")
        );

        Match match = Match.createMatch(member1, member2);
        MatchingReview matchingReview1 = MatchingReview.createMatchingReview(
                match,
                member1,
                member2,
                true,
                true,
                true
        );
        MatchingReview matchingReview2 = MatchingReview.createMatchingReview(
                match,
                member2,
                member1,
                true,
                true,
                true
        );
        //when
        em.persist(member1);
        em.persist(member2);
        em.persist(match);
        em.persist(matchingReview1);
        em.persist(matchingReview2);
        em.persist(matchingReview2);

        //then
    }
}
