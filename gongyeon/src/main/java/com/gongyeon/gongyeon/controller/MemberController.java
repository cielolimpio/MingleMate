package com.gongyeon.gongyeon.controller;

import com.gongyeon.gongyeon.controller.dto.MemberLoginDto;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.security.TokenInfo;
import com.gongyeon.gongyeon.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

//    @PostMapping("/sign-up")
//    public ResponseEntity<Long> signUp(
//            @RequestBody SignUpRequest request
//    ) {
//        Member member = Member.createMember(
//                request.getName(),
//                request.getEmail(),
//                request.getPassword(),
//                request.getGender(),
//                request.getAge(),
//                request.getAddress()
//        );
//        Long id = memberService.signUp(member);
//
//        return ResponseEntity.ok(id);
//    }

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginDto memberLoginDto){
        log.info("login request");
        return memberService.login(memberLoginDto.getEmail(), memberLoginDto.getPassword());
    }
}
