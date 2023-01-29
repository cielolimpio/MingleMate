package com.gongyeon.gongyeon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTag extends BaseEntity {

    @Id @GeneratedValue
    private Long id;


    private Integer attendance = 0;

    private Integer kindness = 0;

    private Integer studyingHard = 0;
}
