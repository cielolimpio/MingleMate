package com.gongyeon.gongyeon.repository;

import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.DaysOfTheWeek;
import com.gongyeon.gongyeon.enums.GenderEnum;
import com.gongyeon.gongyeon.models.MemberProfile;
import com.gongyeon.gongyeon.models.QMemberProfile;
import com.gongyeon.gongyeon.payload.request.SearchProfilesRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.gongyeon.gongyeon.domain.QMember.member;
import static com.gongyeon.gongyeon.domain.QStudyField.studyField;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberProfile> searchProfiles(SearchProfilesRequest request) {
        return queryFactory
                .select(new QMemberProfile(
                        member.id,
                        member.name,
                        member.gender,
                        member.age,
                        member.address,
                        member.possibleDaysOfTheWeek,
                        member.studyFields,
                        member.tags
                ))
                .from(member)
                .innerJoin(member.studyFields, studyField)
                .where(
                        genderEq(request.getGender()),
                        ageGoe(request.getAgeRange().getFirst()),
                        ageLoe(request.getAgeRange().getSecond()),
                        eqAddress(request.getAddress()),
                        neqAnyDaysOfTheWeek(request.getDaysOfTheWeek())
                )
                .offset(0)
                .limit(300)
                .orderBy(member.lastModifiedDateTime.desc())
                .fetch();
    }

    private BooleanExpression genderEq(GenderEnum gender) { return gender != null ? member.gender.eq(gender) : null; }
    private BooleanExpression ageGoe(Integer minAge) { return minAge != null ? member.age.goe(minAge) : null; }
    private BooleanExpression ageLoe(Integer maxAge) { return maxAge != null ? member.age.loe(maxAge) : null; }
    private BooleanExpression eqAddress(Address address) {
        if (address != null) {
            if (address.getCity() != null) {
                BooleanExpression expression = member.address.city.eq(address.getCity());

                if (address.getTown() != null) {
                    expression.and(member.address.town.eq(address.getTown()));

                    if (address.getVillage() != null) {
                        expression.and(member.address.village.eq(address.getVillage()));
                    }
                }

                return expression;
            }
        }
        return null;
    }
    private BooleanExpression neqAnyDaysOfTheWeek(DaysOfTheWeek daysOfTheWeek) {
        if (daysOfTheWeek != null) {

            List<BooleanExpression> boolExpressionList = new ArrayList<>();
            if (daysOfTheWeek.isMon()) boolExpressionList.add(member.possibleDaysOfTheWeek.mon.isFalse());
            if (daysOfTheWeek.isTue()) boolExpressionList.add(member.possibleDaysOfTheWeek.tue.isFalse());
            if (daysOfTheWeek.isWed()) boolExpressionList.add(member.possibleDaysOfTheWeek.wed.isFalse());
            if (daysOfTheWeek.isThu()) boolExpressionList.add(member.possibleDaysOfTheWeek.thu.isFalse());
            if (daysOfTheWeek.isFri()) boolExpressionList.add(member.possibleDaysOfTheWeek.fri.isFalse());
            if (daysOfTheWeek.isSat()) boolExpressionList.add(member.possibleDaysOfTheWeek.sat.isFalse());
            if (daysOfTheWeek.isSun()) boolExpressionList.add(member.possibleDaysOfTheWeek.sun.isFalse());

            return boolExpressionList.stream()
                    .reduce(BooleanExpression::and)
                    .orElse(null);
        } else return null;
    }
}
