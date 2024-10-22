package com.example.dpm.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.dpm.auth.dto.OauthRequestDto;
import com.example.dpm.auth.dto.OauthResponseDto;
import com.example.dpm.auth.dto.TokenResponseDto;
import com.example.dpm.auth.service.KakaoOauthService;
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
    
    @GetMapping("/login/oauth/kakao")
    public OauthResponseDto kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();

        // KakaoOauthService에서 Access/Refresh Token 발급받기
        Map<String, Object> tokenResponse = oauthService.getKakaoToken(code);

        String accessToken = (String) tokenResponse.get("access_token");
        String refreshToken = (String) tokenResponse.get("refresh_token");

        // 응답 객체에 Access Token, Refresh Token 저장
        oauthResponseDto.setAccessToken(accessToken);
        oauthResponseDto.setRefreshToken(refreshToken);
        
        oauthResponseDto.setCode(code);
        System.out.println("OauthController   " + refreshToken + "code: " + code);

        return oauthResponseDto;
    }
    
    @PostMapping("/login/oauth/{provider}")
    public OauthResponseDto login(@PathVariable("provider") String provider, @RequestBody OauthRequestDto oauthRequestDto,
                                  HttpServletResponse response) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();

        switch (provider) {
            case "kakao":
                // loginWithKakao 메서드가 TokenResponseDto 객체를 반환한다고 가정
                TokenResponseDto tokenResponse = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(), response);
                
                String accessToken = tokenResponse.getAccessToken();
                String refreshToken = tokenResponse.getRefreshToken();
                
                System.out.println("#OauthController: accessToken: " + accessToken);
                System.out.println("#OauthController: refreshToken: " + refreshToken);

                // 응답 객체에 AccessToken과 RefreshToken 모두 설정
                oauthResponseDto.setAccessToken(accessToken);
                oauthResponseDto.setRefreshToken(refreshToken);
                break;
            default:
                // 예외 처리를 추가하여 잘못된 provider 요청에 대한 로그 확인
                System.out.println("#OauthController: Provider: " + provider);
                System.out.println("#OauthController: AccessToken: " + oauthRequestDto.getAccessToken());
                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
        }

        return oauthResponseDto;
    }



//    @PostMapping("/login/oauth/{provider}")
//    public OauthResponseDto login(@PathVariable("provider") String provider, @RequestBody OauthRequestDto oauthRequestDto,
//                                  HttpServletResponse response) {
//        OauthResponseDto oauthResponseDto = new OauthResponseDto();
//        switch (provider) {
//            case "kakao":
//                String accessToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(), response);
//                System.out.println("#OauthController: accessToken: " + accessToken);
//                System.out.println("#OauthController: accessToken: " + refreshToken);
//                oauthResponseDto.setAccessToken(accessToken);
//                break;
//            default:
//                // 예외 처리를 추가하여 잘못된 provider 요청에 대한 로그 확인
//            	System.out.println("#OauthController: Provider: " + provider);
//            	System.out.println("#OauthController: AccessToken: " + oauthRequestDto.getAccessToken());
//                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
//        }
//        return oauthResponseDto;
//    }
    
//    @PostMapping("/login/oauth/{provider}")
//    public OauthResponseDto login(@PathVariable("provider") String provider, 
//                                  @RequestBody OauthRequestDto oauthRequestDto,
//                                  HttpServletResponse response) {
//        OauthResponseDto oauthResponseDto = new OauthResponseDto();
//        
//        switch (provider) {
//            case "kakao":
//                // Kakao OAuth를 통해 Access Token과 Refresh Token을 가져옵니다.
//                Map<String, Object> tokens = oauthService.getKakaoCode(oauthRequestDto.getCode());
//                
//                // Access Token과 Refresh Token을 OauthResponseDto에 설정
//                String accessToken = (String) tokens.get("access_token");
//                String refreshToken = (String) tokens.get("refresh_token");
//
//                // Access Token과 Refresh Token이 null인 경우 예외 처리
//                if (accessToken == null || refreshToken == null) {
//                    throw new IllegalArgumentException("Access Token 또는 Refresh Token을 받을 수 없습니다.");
//                }
//
//                oauthResponseDto.setAccessToken(accessToken);
//                oauthResponseDto.setRefreshToken(refreshToken);
//
//                // 로그 출력
//                System.out.println("#OauthController: accessToken: " + accessToken);
//                System.out.println("#OauthController: refreshToken: " + refreshToken);
//                break;
//
//            default:
//                // 잘못된 provider 요청에 대한 예외 처리
//                System.out.println("#OauthController: Provider: " + provider);
//                System.out.println("#OauthController: AccessToken: " + oauthRequestDto.getAccessToken());
//                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
//        }
//        return oauthResponseDto;
//    }



    // 리프레시 토큰으로 액세스토큰 재발급 받는 로직
    @PostMapping("/token/refresh")
    public TokenResponseDto tokenRefresh(Map<String, Object> tokenResponse, HttpServletRequest request) {
        TokenResponseDto refreshTokenResponseDto = new TokenResponseDto();
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
