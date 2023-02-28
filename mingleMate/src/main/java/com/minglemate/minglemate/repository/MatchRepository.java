package com.minglemate.minglemate.repository;

import com.minglemate.minglemate.domain.Match;
import com.minglemate.minglemate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByRequester(Member member);
    List<Match> findByResponder(Member member);
}
