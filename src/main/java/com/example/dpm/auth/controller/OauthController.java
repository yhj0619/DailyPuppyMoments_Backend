package com.example.dpm.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dpm.auth.dto.OauthRequestDto;
import com.example.dpm.auth.dto.OauthResponseDto;
import com.example.dpm.auth.dto.TokenResponseDto;
import com.example.dpm.auth.service.JwtTokenService;
import com.example.dpm.auth.service.KakaoOauthService;
import com.example.dpm.auth.service.OauthService;
import com.example.dpm.exception.CustomException;
import com.example.dpm.exception.ErrorCode;
import com.example.dpm.member.service.MemberService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    
    @GetMapping("/login/oauth/kakao")
    public OauthResponseDto kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();

        // KakaoOauthService에서 Access/Refresh Token 발급받기
        Map<String, Object> tokenResponse = oauthService.getKakaoToken(code);

        String accessToken = (String) tokenResponse.get("access_token");
        String refreshToken = (String) tokenResponse.get("refresh_token");

        // 응답 객체에 Access Token, Refresh Token 저장
        oauthResponseDto.setAccessToken(accessToken);
        oauthResponseDto.setRefreshToken(refreshToken);
        
        //oauthResponseDto.setCode(code);
        System.out.println("OauthController   " + refreshToken + "code: " + code);
        System.out.println("ffffffffffffffffffffff  " + ResponseEntity.ok(oauthResponseDto));
        
        String redirectUrl = "http://localhost:5174?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
        //response.sendRedirect(redirectUrl);
        return oauthResponseDto;
    }
    
    @PostMapping("/login/oauth/{provider}")
    public String login(@PathVariable("provider") String provider, @RequestBody OauthRequestDto oauthRequestDto,
                                  HttpServletResponse response) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        String jwtToken = "";
        
        switch (provider) {
            case "kakao":
                // loginWithKakao 메서드가 TokenResponseDto 객체를 반환한다고 가정
                 jwtToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(), response);
                break;

            default:
                // 예외 처리를 추가하여 잘못된 provider 요청에 대한 로그 확인
                System.out.println("#OauthController: Provider: " + provider);
                System.out.println("#OauthController: AccessToken: " + oauthRequestDto.getAccessToken());
                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
        }
        
        
        return jwtToken;
    }
   
}
