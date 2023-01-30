package com.gongyeon.gongyeon.controller;

import com.gongyeon.gongyeon.domain.embeddedTypes.Address;
import com.gongyeon.gongyeon.enums.GenderEnum;
import com.gongyeon.gongyeon.payload.request.SignUpRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberControllerTest {

    @Autowired
    MemberController memberController;
    @PersistenceContext
    EntityManager em;

//    @Test
//    public void signUp() throws Exception {
//        //given
//        SignUpRequest signUpRequest = new SignUpRequest(
//                "test",
//                "test@gmail.com",
//                "password",
//                GenderEnum.MALE,
//                25,
//                new Address("city", "town", "village")
//        );
//
//        //when
//        ResponseEntity<Long> response = memberController.signUp(signUpRequest);
//
//        //then
//        Assertions.assertThat(response.getBody()).isEqualTo(1);
//    }
}
