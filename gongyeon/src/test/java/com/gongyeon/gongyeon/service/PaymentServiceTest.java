package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.GongyeonBaseTest;
import com.gongyeon.gongyeon.GongyeonTestHelper;
import com.gongyeon.gongyeon.domain.Match;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.domain.Payment;
import com.gongyeon.gongyeon.enums.MatchingStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import com.gongyeon.gongyeon.models.PaymentDto;
import com.gongyeon.gongyeon.models.PaymentResponse;
import com.gongyeon.gongyeon.repository.MatchRepository;
import com.gongyeon.gongyeon.repository.PaymentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


class PaymentServiceTest extends GongyeonBaseTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    MatchService matchService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    MatchRepository matchRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 결제_진행(){
        //given
        Member responder = signUp("responder", "responder@naver.com", "responder");
        Member requester = signUpAndLoginWithAuth("requester", "requester@naver.com", "requester");

        Match match = Match.createMatch(requester, responder);
        em.persist(match);

        matchService.matchComplete(match.getId());

        //when
        PaymentResponse paymentResponse = paymentService.savePayment(
                new PaymentDto(requester.getId(), 3000),
                match.getId()
        );

        em.flush();
        em.clear();

        Payment payment = paymentRepository.findById(paymentResponse.getPaymentId()).get();

        //then
        assertThat(payment.getAmount()).isEqualTo(3000);
        assertThat(payment.getMatch().getMatchingStatus()).isEqualTo(MatchingStatusEnum.PAYMENT_COMPLETE);
        assertThat(payment.getMember().getName()).isEqualTo("requester");

    }

    @Test
    public void 결제_취소(){
        //given
        Member responder = signUp("responder", "responder@naver.com", "responder");
        Member requester = signUpAndLoginWithAuth("requester", "requester@naver.com", "requester");

        Match match = Match.createMatch(requester, responder);
        em.persist(match);

        matchService.matchComplete(match.getId());

        PaymentResponse savePayment = paymentService.savePayment(
                new PaymentDto(requester.getId(), 3000),
                match.getId()
        );

        //when
        paymentService.cancelPayment(savePayment.getPaymentId());

        em.flush();
        em.clear();

        Match findMatch = matchRepository.findById(match.getId()).get();


        //then
        assertThat(findMatch.getMatchingStatus()).isEqualTo(MatchingStatusEnum.PAYMENT_CANCEL);
    }
}