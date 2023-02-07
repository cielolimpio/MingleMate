package com.gongyeon.gongyeon.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(name = "registered_datetime", nullable = false)
    private LocalDateTime registeredDateTime;

    @LastModifiedDate
    @Column(name = "last_modified_datetime", nullable = false)
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "deleted_datetime")
    private LocalDateTime deletedDateTime;
}
