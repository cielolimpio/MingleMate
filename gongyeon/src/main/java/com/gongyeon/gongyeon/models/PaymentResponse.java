package com.gongyeon.gongyeon.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private MatchDto matchDto;
    private int amount;
}
