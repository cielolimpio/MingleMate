package com.gongyeon.gongyeon.controller;

import com.gongyeon.gongyeon.domain.Match;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.models.MatchDto;
import com.gongyeon.gongyeon.provider.AuthenticationProvider;
import com.gongyeon.gongyeon.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/request/{responderId}")
    public MatchDto matchRequest(@PathVariable Long responderId){
        return matchService.matchRequest(responderId);
    }

    @PostMapping("/complete/{matchId}")
    public MatchDto matchComplete(@PathVariable Long matchId){
        return matchService.matchComplete(matchId);
    }

    @PostMapping("/reject/{matchId}")
    public MatchDto matchReject(@PathVariable Long matchId){
        return matchService.matchReject(matchId);
    }

}
