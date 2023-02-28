package com.minglemate.minglemate.service;

import com.minglemate.minglemate.domain.Match;
import com.minglemate.minglemate.domain.Member;
import com.minglemate.minglemate.domain.Payment;
import com.minglemate.minglemate.enums.HttpStatusEnum;
import com.minglemate.minglemate.enums.MatchStatusEnum;
import com.minglemate.minglemate.exception.MingleMateException;
import com.minglemate.minglemate.models.MatchDto;
import com.minglemate.minglemate.models.PaymentResponse;
import com.minglemate.minglemate.provider.AuthenticationProvider;
import com.minglemate.minglemate.repository.MatchRepository;
import com.minglemate.minglemate.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public PaymentResponse savePayment(Long matchId, int amount){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "매칭 정보가 존재하지 않습니다."));
        Member requester = match.getRequester();

        if(!requester.getId().equals(AuthenticationProvider.getCurrentMemberId())){
            throw new MingleMateException(HttpStatusEnum.BAD_REQUEST, "잘못된 결제 저장 요청입니다.");
        }

        Payment payment = Payment.createPayment(match, requester, amount);
        paymentRepository.save(payment);

        match.changeMatchStatus(MatchStatusEnum.PAYMENT_COMPLETE);
        return new PaymentResponse(
                payment.getId(),
                new MatchDto(requester.getName(),
                        match.getResponder().getName(),
                        match.getStatus(),
                        match.getLastModifiedDateTime()),
                amount
        );
    }

    @Transactional
    public PaymentResponse cancelPayment(Long paymentId){
        Payment cancelPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new MingleMateException(HttpStatusEnum.NOT_FOUND, "결제 정보가 존재하지 않습니다."));
        Match match = cancelPayment.getMatch();
        match.changeMatchStatus(MatchStatusEnum.PAYMENT_CANCEL);

        paymentRepository.delete(cancelPayment);

        return new PaymentResponse(
                cancelPayment.getId(),
                new MatchDto(match.getRequester().getName(),
                        match.getResponder().getName(),
                        match.getStatus(),
                        match.getLastModifiedDateTime()),
                0
        );
    }



}
