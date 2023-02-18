package com.gongyeon.gongyeon.service;

import com.gongyeon.gongyeon.domain.Match;
import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.domain.Payment;
import com.gongyeon.gongyeon.enums.HttpStatusEnum;
import com.gongyeon.gongyeon.enums.MatchingStatusEnum;
import com.gongyeon.gongyeon.exception.GongYeonException;
import com.gongyeon.gongyeon.models.MatchDto;
import com.gongyeon.gongyeon.models.PaymentDto;
import com.gongyeon.gongyeon.models.PaymentResponse;
import com.gongyeon.gongyeon.provider.AuthenticationProvider;
import com.gongyeon.gongyeon.repository.MatchRepository;
import com.gongyeon.gongyeon.repository.PaymentRepository;
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
                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.NOT_FOUND, "매칭 정보가 존재하지 않습니다."));
        Member requester = match.getRequester();

        if(!requester.getId().equals(AuthenticationProvider.getCurrentMemberId())){
            throw new GongYeonException(HttpStatusEnum.BAD_REQUEST, "잘못된 결제 저장 요청입니다.");
        }

        Payment payment = Payment.createPayment(match, requester, amount);
        paymentRepository.save(payment);

        match.changeMatchStatus(MatchingStatusEnum.PAYMENT_COMPLETE);
        return new PaymentResponse(
                payment.getId(),
                new MatchDto(requester.getName(),
                        match.getResponder().getName(),
                        match.getMatchingStatus(),
                        match.getLastModifiedDateTime()),
                amount
        );
    }

    @Transactional
    public PaymentResponse cancelPayment(Long paymentId){
        Payment cancelPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new GongYeonException(HttpStatusEnum.NOT_FOUND, "결제 정보가 존재하지 않습니다."));
        Match match = cancelPayment.getMatch();
        match.changeMatchStatus(MatchingStatusEnum.PAYMENT_CANCEL);

        paymentRepository.delete(cancelPayment);

        return new PaymentResponse(
                cancelPayment.getId(),
                new MatchDto(match.getRequester().getName(),
                        match.getResponder().getName(),
                        match.getMatchingStatus(),
                        match.getLastModifiedDateTime()),
                0
        );
    }



}
