package com.gongyeon.gongyeon.controller;

import com.gongyeon.gongyeon.models.MatchDto;
import com.gongyeon.gongyeon.models.MemberProfile;
import com.gongyeon.gongyeon.models.MyPageDto;
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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public TokenInfo signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        Member member = Member.createMember(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        return memberService.signUp(member);
    }

    @PostMapping("/login")
    public TokenInfo login(@Valid @RequestBody MemberLoginRequest memberLoginRequest){
        log.info("Login Request");
        return memberService.login(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());
    }

    @PostMapping("/reissue")
    public TokenInfo reissue(@Valid @RequestBody TokenInfo tokenInfo){
        log.info("Reissue Request");
        return memberService.reissue(tokenInfo);
    }
    
    
    @PostMapping("/logout")
    public void logout(){
        log.info("Logout Request");
        memberService.logout();
    }

    @PostMapping("/search-profiles")
    public Page<MemberProfile> searchProfiles(@Valid @RequestBody SearchProfilesRequest request, Pageable pageable) {
        return memberService.searchProfiles(request, pageable);
    }

    @PostMapping("/update-profiles")
    public MemberProfile updateProfiles(@RequestBody MemberProfile updateProfile){
        return memberService.updateProfiles(updateProfile);
    }


    @GetMapping("/mypage")
    public MyPageDto myPage(){
        return memberService.myPage();
    }

    @GetMapping("/mypage/matching-history")
    public List<MatchDto> matchingHistory(){
        return memberService.matchingHistory();
    }

}
