package com.gongyeon.gongyeon.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Long memberId;
    private int amount;
}
