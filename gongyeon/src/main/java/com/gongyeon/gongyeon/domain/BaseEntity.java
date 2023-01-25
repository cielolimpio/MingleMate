package com.gongyeon.gongyeon.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(name = "registered_datetime")
    private LocalDateTime registeredDateTime;

    @LastModifiedDate
    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "deleted_datetime")
    private LocalDateTime deletedDateTime;
}
