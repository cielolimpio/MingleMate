package com.gongyeon.gongyeon.provider;

import com.gongyeon.gongyeon.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider {
    public static UserDetailsImpl getCurrentMember() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getCurrentMemberId() {
        return getCurrentMember().getMemberId();
    }
}
