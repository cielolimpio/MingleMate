package com.gongyeon.gongyeon.controller;

import com.gongyeon.gongyeon.models.MemberProfile;
import com.gongyeon.gongyeon.payload.request.MemberLoginRequest;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.payload.request.SearchProfilesRequest;
import com.gongyeon.gongyeon.payload.request.SignUpRequest;
import com.gongyeon.gongyeon.models.TokenInfo;
import com.gongyeon.gongyeon.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public SignUpRequest signUp(
            @RequestBody SignUpRequest request
    ) {
        Member member = Member.createMember(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        memberService.signUp(member);
        return request;
    }

    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginRequest memberLoginRequest){
        log.info("Login Request");
        return memberService.login(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());
    }

    @PostMapping("/reissue")
    public TokenInfo reissue(@RequestBody TokenInfo tokenInfo){
        log.info("Reissue Request");
        return memberService.reissue(tokenInfo);
    }
    
    
    @PostMapping("/logout")
    public void logout(@RequestHeader(value = "Authorization") String accessTokenWithType){
        log.info("Logout Request");
        String accessToken = accessTokenWithType.substring(7);
        memberService.logout(accessToken);
    }

    @GetMapping("/search-profiles")
    public Page<MemberProfile> searchProfiles(SearchProfilesRequest request, Pageable pageable) {
        return memberService.searchProfiles(request, pageable);
    }
}
