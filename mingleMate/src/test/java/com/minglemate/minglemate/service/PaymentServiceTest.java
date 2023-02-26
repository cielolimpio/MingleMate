package com.minglemate.minglemate.service;

import com.minglemate.minglemate.MingleMateBaseTest;
import com.minglemate.minglemate.domain.Match;
import com.minglemate.minglemate.domain.Member;
import com.minglemate.minglemate.domain.Payment;
import com.minglemate.minglemate.enums.MatchStatusEnum;
import com.minglemate.minglemate.models.PaymentResponse;
import com.minglemate.minglemate.repository.MatchRepository;
import com.minglemate.minglemate.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;


class PaymentServiceTest extends MingleMateBaseTest {

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

        matchService.changeMatchStatus(match.getId());

        //when
        PaymentResponse paymentResponse = paymentService.savePayment(
                match.getId(),
                3000
        );

        em.flush();
        em.clear();

        Payment payment = paymentRepository.findById(paymentResponse.getPaymentId()).get();

        //then
        assertThat(payment.getAmount()).isEqualTo(3000);
        assertThat(payment.getMatch().getStatus()).isEqualTo(MatchStatusEnum.PAYMENT_COMPLETE);
        assertThat(payment.getMember().getName()).isEqualTo("requester");

    }

    @Test
    public void 결제_취소(){
        //given
        Member responder = signUp("responder", "responder@naver.com", "responder");
        Member requester = signUpAndLoginWithAuth("requester", "requester@naver.com", "requester");

        Match match = Match.createMatch(requester, responder);
        em.persist(match);

        matchService.changeMatchStatus(match.getId());

        PaymentResponse savePayment = paymentService.savePayment(
                match.getId(),
                3000
        );

        //when
        paymentService.cancelPayment(savePayment.getPaymentId());

        em.flush();
        em.clear();

        Match findMatch = matchRepository.findById(match.getId()).get();


        //then
        assertThat(findMatch.getStatus()).isEqualTo(MatchStatusEnum.PAYMENT_CANCEL);
    }
}