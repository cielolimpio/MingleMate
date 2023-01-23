package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import com.gongyeon.gongyeon.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long signUp(Member member) {
        validateDuplicateMember(member);
        Member newMember = memberRepository.save(member);
        return newMember.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> members = memberRepository.findByEmail(member.getEmail());
        if (!members.isEmpty())
            throw new GongYeonException(HttpStatusEnum.CONFLICT, "Duplicate Member");
    }
}
