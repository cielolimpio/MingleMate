package com.mingleMate.mingleMate.provider;

import com.mingleMate.mingleMate.domain.Member;
import com.mingleMate.mingleMate.security.UserDetailsImpl;
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
