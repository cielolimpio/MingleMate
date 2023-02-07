package com.gongyeon.gongyeon.repository;

import com.gongyeon.gongyeon.domain.Match;
import com.gongyeon.gongyeon.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByRequester(Member member);
    List<Match> findByResponder(Member member);
}
