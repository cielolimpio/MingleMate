package com.minglemate.minglemate.controller;

import com.minglemate.minglemate.models.*;
import com.minglemate.minglemate.models.payload.request.*;
import com.minglemate.minglemate.domain.Member;
import com.minglemate.minglemate.models.payload.response.MemberProfileResponse;
import com.minglemate.minglemate.models.payload.response.MyPageResponse;
import com.minglemate.minglemate.service.MemberService;
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

    @PostMapping("/profile-images")
    public void updateProfileImages(@RequestBody UpdateProfileImagesRequest request) {
        memberService.updateProfileImages(request);
    }

    @PostMapping("/profile")
    public void updateProfile(@RequestBody UpdateProfileRequest updateProfile) {
        memberService.updateProfile(updateProfile);
    }

    @GetMapping("/my-page")
    public MyPageResponse myPage(){
        return memberService.myPage();
    }

    @PostMapping("/search-profiles")
    public Page<MemberProfileResponse> searchProfiles(@Valid @RequestBody SearchProfilesRequest request, Pageable pageable) {
        return memberService.searchProfiles(request, pageable);
    }

    @GetMapping("/my-page/match-history")
    public List<MatchDto> matchHistory() {
        return memberService.matchHistory();
    }
}
