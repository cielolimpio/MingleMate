package com.mingleMate.mingleMate.controller;

import com.mingleMate.mingleMate.models.*;
import com.mingleMate.mingleMate.models.payload.request.MemberLoginRequest;
import com.mingleMate.mingleMate.domain.Member;
import com.mingleMate.mingleMate.models.payload.request.SearchProfilesRequest;
import com.mingleMate.mingleMate.models.payload.request.SignUpRequest;
import com.mingleMate.mingleMate.models.payload.request.UpdateProfileRequest;
import com.mingleMate.mingleMate.models.payload.response.MyPageResponse;
import com.mingleMate.mingleMate.service.MemberService;
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

    @PostMapping("/update-profiles")
    public UpdateProfileRequest updateProfiles(@RequestBody UpdateProfileRequest updateProfile){
        return memberService.updateProfiles(updateProfile);
    }

    @GetMapping("/my-page")
    public MyPageResponse myPage(){
        return memberService.myPage();
    }

    @PostMapping("/search-profiles")
    public Page<MemberProfile> searchProfiles(@Valid @RequestBody SearchProfilesRequest request, Pageable pageable) {
        return memberService.searchProfiles(request, pageable);
    }

    @GetMapping("/my-page/matching-history")
    public List<MatchDto> matchingHistory(){
        return memberService.matchingHistory();
    }

}
