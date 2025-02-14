package com.minglemate.minglemate;

import com.minglemate.minglemate.domain.Category;
import com.minglemate.minglemate.domain.Member;
import com.minglemate.minglemate.domain.MemberCategory;
import com.minglemate.minglemate.domain.embeddedTypes.Address;
import com.minglemate.minglemate.enums.ParentCategoryEnum;
import com.minglemate.minglemate.enums.GenderEnum;
import com.minglemate.minglemate.models.TokenInfo;
import com.minglemate.minglemate.repository.MemberRepository;
import com.minglemate.minglemate.security.JwtTokenProvider;
import com.minglemate.minglemate.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MingleMateTestHelper {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    public void createBaseMembers(List<Category> baseStudyFields) {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = createMember(i, baseStudyFields);
            em.persist(member);
        });
    }

    public List<Category> createBaseStudyFields() {
        List<ParentCategoryEnum> categories = List.of(ParentCategoryEnum.values());
        return categories.stream().map(category -> IntStream.rangeClosed(1, 3).mapToObj(sfi -> {
            Category studyField = Category.createCategory(category.name() + sfi, null, category);
            em.persist(studyField);
            return studyField;
        }).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private Member createMember(int i, List<Category> baseStudyFields) {
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

        List<MemberCategory> memberStudyFieldList = IntStream.rangeClosed(1, 3).mapToObj(sfi -> {
            Random rand = new Random(System.currentTimeMillis());
            int index = rand.nextInt(baseStudyFields.size());
            Category studyField = baseStudyFields.get(index);
            MemberCategory memberStudyField = MemberCategory.createMemberCategory(member, studyField, studyField.getMainName());
            em.persist(memberStudyField);
            return memberStudyField;
        }).collect(Collectors.toList());

        member.updateMember(member.getName(), gender, age, address, daysOfTheWeek, memberStudyFieldList);
        return member;
    }

    public Member signUp(String username, String email, String password){
        Member newMember = Member.createMember(username, email, password);
        memberService.signUp(newMember);
        return memberRepository.findByEmail(newMember.getEmail()).get();
    }

    public Member signUpAndLoginWithAuth(String username, String email, String password) {
        Member newMember = Member.createMember(username, email, password);
        memberService.signUp(newMember);
        TokenInfo tokenInfo = memberService.login(newMember.getEmail(), newMember.getPassword());
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return memberRepository.findByEmail(newMember.getEmail()).get();
    }
}
