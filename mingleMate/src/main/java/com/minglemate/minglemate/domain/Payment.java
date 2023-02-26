package com.minglemate.minglemate.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
    private int amount;

    @Column(name = "deleted_datetime")
    private LocalDateTime deletedDateTime;

    public static Payment createPayment(Match match, Member member, int amount){
        Payment payment = new Payment();
        payment.match = match;
        payment.member = member;
        payment.amount = amount;
        return payment;
    }

}
