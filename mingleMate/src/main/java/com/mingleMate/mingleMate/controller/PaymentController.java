package com.mingleMate.mingleMate.controller;

import com.mingleMate.mingleMate.models.PaymentResponse;
import com.mingleMate.mingleMate.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    /** Iamport 결제 검증 컨트롤러 **/
    private final IamportClient iamportClient;
    private final PaymentService paymentService;


    // 생성자를 통해 REST API 와 REST API secret 입력
    @Autowired
    public PaymentController(@Value("${imp.restApiKey}") String restApiKey,
                             @Value("${imp.restApiSecretKey}") String restApiSecretKey,
                             PaymentService paymentService){
        this.iamportClient = new IamportClient(restApiKey, restApiSecretKey);
        this.paymentService = paymentService;
    }

    /** 프론트에서 받은 PG사 결괏값을 통해 아임포트 토큰 발행 **/
    @PostMapping("/verify/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable String imp_uid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }

    @PostMapping("/{matchId}")
    public PaymentResponse doPayment(
            @PathVariable Long matchId,
            @RequestParam int amount){
        return paymentService.savePayment(matchId, amount);
    }

    @PostMapping("/cancel/{paymentId}")
    public PaymentResponse cancelPayment(@PathVariable Long paymentId){
        return paymentService.cancelPayment(paymentId);
    }


}
