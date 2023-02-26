package com.mingleMate.mingleMate.domain.embeddedTypes;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class Address {
    @Column(length = 20)
    private String city;
    @Column(length = 20)
    private String town;
    @Column(length = 20)
    private String village;

    protected Address() {}

    public Address(String city, String town, String village) {
        this.city = city;
        this.town = town;
        this.village = village;
    }
}
