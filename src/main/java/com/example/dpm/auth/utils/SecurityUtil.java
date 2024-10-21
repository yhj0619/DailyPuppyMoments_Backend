package com.example.dpm.auth.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.dpm.auth.models.UserPrincipal;
import com.example.dpm.exception.CustomException;
import com.example.dpm.exception.ErrorCode;

public class SecurityUtil {
    private SecurityUtil() {}

    public static Long getCurrentUserId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Long userId;
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getId();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return (Long) userId;
    }
}
