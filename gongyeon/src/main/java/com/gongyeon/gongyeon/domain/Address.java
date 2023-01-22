package com.gongyeon.gongyeon.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class Address {
    private String city;
    private String town;
    private String village;

    protected Address() {}

    public Address(String city, String town, String village) {
        this.city = city;
        this.town = town;
        this.village = village;
    }
}
