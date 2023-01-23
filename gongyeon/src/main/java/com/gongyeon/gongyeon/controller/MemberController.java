package com.gongyeon.gongyeon.controller;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.payload.request.SignUpRequest;
import com.gongyeon.gongyeon.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<Long> signUp(
            @RequestBody SignUpRequest request
    ) {
        Member member = Member.createMember(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getGender(),
                request.getAge(),
                request.getAddress()
        );
        Long id = memberService.signUp(member);

        return ResponseEntity.ok(id);
    }
}
