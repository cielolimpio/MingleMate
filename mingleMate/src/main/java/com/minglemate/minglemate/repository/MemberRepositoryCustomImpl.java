package com.minglemate.minglemate.repository;

import com.minglemate.minglemate.domain.Category;
import com.minglemate.minglemate.domain.QProfileImage;
import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.enums.PeriodOptionEnum;
import com.minglemate.minglemate.models.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.minglemate.minglemate.domain.QCategory.category;
import static com.minglemate.minglemate.domain.QMember.member;
import static com.minglemate.minglemate.domain.QMemberCategory.memberCategory;
import static com.minglemate.minglemate.domain.QProfileImage.profileImage;


public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberProfile> searchProfiles(SearchProfilesDto searchProfilesDto, Pageable pageable) {
        List<MemberProfile> content = queryFactory
                .selectFrom(member)
                .leftJoin(member.profileImages, profileImage)
                .leftJoin(member.memberCategories, memberCategory)
                .innerJoin(memberCategory.category, category)
                .where(
                        genderEq(searchProfilesDto.getGender()),
                        ageGoe(searchProfilesDto.getAgeRange().getFirst()),
                        ageLoe(searchProfilesDto.getAgeRange().getSecond()),
                        eqAddress(searchProfilesDto.getAddress()),
                        eqCategory(searchProfilesDto.getCategory()),
                        eqPeriodOption(searchProfilesDto.getPeriodOption())
                )
                .groupBy(member.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.registeredDateTime.desc())
                .fetch()
                .stream().map(m -> new MemberProfile(
                        m.getId(),
                        m.getName(),
                        m.getGender(),
                        m.getAge(),
                        m.getAddress(),
                        m.getProfileImages().stream().map(ProfileImageDto::new).collect(Collectors.toList()),
                        m.getMemberCategories().stream().map(CategoryDto::new).collect(Collectors.toList())
                )).collect(Collectors.toList());

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.memberCategories, memberCategory)
                .innerJoin(memberCategory.category, category)
                .where(
                        genderEq(searchProfilesDto.getGender()),
                        ageGoe(searchProfilesDto.getAgeRange().getFirst()),
                        ageLoe(searchProfilesDto.getAgeRange().getSecond()),
                        eqAddress(searchProfilesDto.getAddress()),
                        eqCategory(searchProfilesDto.getCategory()),
                        eqPeriodOption(searchProfilesDto.getPeriodOption())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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
    private BooleanExpression eqCategory(Category c) {
        if (c.getName() == null) return category.parentCategory.eq(c.getParentCategory());
        else return category.id.eq(c.getId());
    }
    private BooleanExpression eqPeriodOption(PeriodOptionEnum periodOption) {
        return memberCategory.periodOption.eq(periodOption);
    }
}
