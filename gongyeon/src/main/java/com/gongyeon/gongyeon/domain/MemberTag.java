package com.gongyeon.gongyeon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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

    @Nullable
    private int attendance = 0;
    @Nullable
    private int kindness = 0;
    @Nullable
    private int studyingHard = 0;
}
