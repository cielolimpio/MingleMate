package com.gongyeon.gongyeon.controller;

import com.gongyeon.gongyeon.payload.request.MemberLoginRequest;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.enums.RoleEnum;
import com.gongyeon.gongyeon.payload.request.SignUpRequest;
import com.gongyeon.gongyeon.repository.MemberRepository;
import com.gongyeon.gongyeon.models.TokenInfo;
import com.gongyeon.gongyeon.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
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
        member.changeRole(RoleEnum.USER);
        Long id = memberService.signUp(member);

        return ResponseEntity.ok(id);
    }

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginRequest memberLoginRequest){
        log.info("Login Request");
        return memberService.login(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenInfo tokenInfo){
        log.info("Reissue Request");
        return memberService.reissue(tokenInfo);
    }
    
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String accessTokenWithType){
        log.info("Logout Request");
        String accessToken = accessTokenWithType.substring(7);
        return memberService.logout(accessToken);
    }

//    @GetMapping("/user")
//    public String user(){
//        return "ok";
//    }
//
//    @GetMapping("/admin")
//    public String admin(){
//        return "ok";
//    }
//
//    @PostConstruct
//    public void init(){
//        Member member = Member.createMember("동현", "test@naver.com", "1234", GenderEnum.MALE, 30, null);
//        member.changeRole(RoleEnum.USER);
//        memberRepository.save(member);
//    }


}
