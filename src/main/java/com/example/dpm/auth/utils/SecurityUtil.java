package com.example.dpm.auth.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.dpm.auth.exception.CustomException;
import com.example.dpm.auth.exception.ErrorCode;
import com.example.dpm.auth.models.UserPrincipal;

import java.util.Optional;


public class SecurityUtil {
    private SecurityUtil() {}

    public static int getCurrentUserId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        int userId;
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getId();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return userId;
    }
}
