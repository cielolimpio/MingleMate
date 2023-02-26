package com.mingleMate.mingleMate.repository;

import com.mingleMate.mingleMate.domain.Match;
import com.mingleMate.mingleMate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByRequester(Member member);
    List<Match> findByResponder(Member member);
}
