package com.gongyeon.gongyeon.repository;

import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.domain.embeddedTypes.Days;
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
                        member.possibleDays,
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
                        neqAnyDays(request.getDays())
                )
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
    private BooleanExpression neqAnyDays(Days days) {
        if (days != null) {
            List<BooleanExpression> boolExpressionList = new ArrayList<>();
            if (days.isMon()) boolExpressionList.add(member.possibleDays.mon.isFalse());
            if (days.isTue()) boolExpressionList.add(member.possibleDays.tue.isFalse());
            if (days.isWed()) boolExpressionList.add(member.possibleDays.wed.isFalse());
            if (days.isThu()) boolExpressionList.add(member.possibleDays.thu.isFalse());
            if (days.isFri()) boolExpressionList.add(member.possibleDays.fri.isFalse());
            if (days.isSat()) boolExpressionList.add(member.possibleDays.sat.isFalse());
            if (days.isSun()) boolExpressionList.add(member.possibleDays.sun.isFalse());

            return boolExpressionList.stream()
                    .reduce(BooleanExpression::and)
                    .orElse(null);
        } else return null;
    }
}
