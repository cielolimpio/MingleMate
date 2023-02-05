package com.gongyeon.gongyeon.provider;

import com.gongyeon.gongyeon.domain.Member;
import com.gongyeon.gongyeon.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider {
    public static Member getCurrentMember() {
        UserDetailsImpl userDetails =  (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMember();
    }

    public static Long getCurrentMemberId() {
        return getCurrentMember().getId();
    }
}
