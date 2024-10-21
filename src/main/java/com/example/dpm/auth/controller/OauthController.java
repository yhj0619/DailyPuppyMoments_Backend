package com.example.dpm.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.dpm.auth.dto.OauthRequestDto;
import com.example.dpm.auth.dto.OauthResponseDto;
import com.example.dpm.auth.dto.RefreshTokenResponseDto;
import com.example.dpm.auth.service.OauthService;
import com.example.dpm.exception.CustomException;
import com.example.dpm.exception.ErrorCode;
import com.example.dpm.member.service.MemberService;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;
    
    private final MemberService memberService;

    @PostMapping("/login/oauth/{provider}")
    public OauthResponseDto login(@PathVariable("provider") String provider, @RequestBody OauthRequestDto oauthRequestDto,
                                  HttpServletResponse response) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        switch (provider) {
            case "kakao":
                String accessToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), response);
                System.out.println("#OauthController: accessToken: " + accessToken);
                oauthResponseDto.setAccessToken(accessToken);
                break;
            default:
                // 예외 처리를 추가하여 잘못된 provider 요청에 대한 로그 확인
            	System.out.println("#OauthController: Provider: " + provider);
            	System.out.println("#OauthController: AccessToken: " + oauthRequestDto.getAccessToken());
                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
        }
        return oauthResponseDto;
    }

    // 리프레시 토큰으로 액세스토큰 재발급 받는 로직
    @PostMapping("/token/refresh")
    public RefreshTokenResponseDto tokenRefresh(Map<String, Object> tokenResponse, HttpServletRequest request) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();
        if(list == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(Collectors.toList()).get(0);

        if(refreshTokenCookie == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponseDto.setAccessToken(accessToken);
        return refreshTokenResponseDto;
    }
}
