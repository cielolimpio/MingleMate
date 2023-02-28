package com.minglemate.minglemate.controller;

import com.minglemate.minglemate.enums.MatchStatusEnum;
import com.minglemate.minglemate.models.MatchDto;
import com.minglemate.minglemate.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/request/{responderId}")
    public MatchDto matchRequest(@PathVariable Long responderId){
        return matchService.matchRequest(responderId);
    }

    @PutMapping("/status")
    public MatchDto changeMatchStatus(
            @RequestParam("matchStatus") Long matchId,
            @RequestParam("matchStatus") MatchStatusEnum matchStatus
    ) {
        return matchService.changeMatchStatus(matchId, matchStatus);
    }
}
