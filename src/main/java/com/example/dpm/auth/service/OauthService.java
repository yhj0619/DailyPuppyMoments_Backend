package com.example.dpm.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.dpm.auth.dto.TokenResponseDto;
import com.example.dpm.exception.CustomException;
import com.example.dpm.exception.ErrorCode;
import com.example.dpm.member.dto.MemberDto;
import com.example.dpm.member.service.MemberService;

@RequiredArgsConstructor
@Service
public class OauthService {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    private final KakaoOauthService kakaoOauthService;
    
    public Map<String, Object> getKakaoToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        // 토큰 요청 파라미터 설정
        return webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", "d8fabac493f22b719a1bc4f29b44c9d1")
                        .with("redirect_uri", "http://localhost:8080/login/oauth/kakao")
                        .with("code", code))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
    
    public Map<String, Object> getKakaoCode(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // JSON 형식으로 설정
                .build();

        // JSON 형식으로 body 설정
        String jsonBody = String.format("{\"code\":\"%s\"}", code);
        
        System.out.println("???OauthService: " + jsonBody);
        System.out.println("???OauthService: " + webClient);

        return webClient.post()
                .uri("/login/oauth/kakao")
                .bodyValue(jsonBody)  // raw JSON 형식으로 body 전송
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }	


    //카카오 로그인
//    public String loginWithKakao(String accessToken,String refreshToken, HttpServletResponse response) {
//    	System.out.println("#OauthService: 또여기????");
//        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken,refreshToken);
//        System.out.println("#OauthService: access token????" + accessToken);
//        System.out.println("#OauthService: refreshToken????" + refreshToken);
//        return accessToken;
//    }
    
    public TokenResponseDto loginWithKakao(String accessToken, String refreshToken, HttpServletResponse response) {
        System.out.println("#OauthService: 또여기????");
        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken, refreshToken);
        System.out.println("#OauthService: access token????" + accessToken);
        System.out.println("#OauthService: refreshToken????" + refreshToken);

     // TokenResponseDto 생성
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setAccessToken(accessToken);
        tokenResponseDto.setRefreshToken(refreshToken);

        return tokenResponseDto;  // TokenResponseDto 객체 반환
    }

    
 // 카카오 로그인
//    public Map<String, String> loginWithKakao(String authorizationCode, HttpServletResponse response) {
//        // 1. Authorization Code로 Access Token 및 Refresh Token 가져오기
//        Map<String, Object> tokenResponse = getKakaoToken(authorizationCode);
//        System.out.println("#OauthService  authorizationCode: " + authorizationCode);
//        
//        
//        String accessToken = (String) tokenResponse.get("access_token");
//        String refreshToken = (String) tokenResponse.get("refresh_token");
//
//        System.out.println("#OauthService: accessToken: " + accessToken);
//        System.out.println("#OauthService: refreshToken: " + refreshToken);
//
//        // 2. Access Token으로 유저 프로필 정보 가져오기
//        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken);
//        System.out.println("#OauthService: User Profile: " + memberDto);
//
//        // 3. Access Token과 Refresh Token을 함께 반환
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("accessToken", accessToken);
//        tokens.put("refreshToken", refreshToken);
//        
//        return tokens;
//    }


 // 액세스토큰, 리프레시토큰 생성
    public String getTokens(Long id, HttpServletResponse response) {
        final String accessToken = jwtTokenService.createAccessToken(String.valueOf(id));
        final String refreshToken = jwtTokenService.createRefreshToken();

        MemberDto memberDto = memberService.findById(id);
        memberDto.setRefreshToken(refreshToken);  // refresh_token을 memberDto에 설정
        memberService.updateRefreshToken(memberDto.getMember_id(), refreshToken);  // DB에 업데이트

        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
        return accessToken;
    }

    // 리프레시 토큰으로 액세스토큰 새로 갱신
    public String refreshAccessToken(String refreshToken) {
        MemberDto memberDto = memberService.getMemberDtoFromRefreshToken(refreshToken);
        if(memberDto == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if(!jwtTokenService.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return jwtTokenService.createAccessToken(String.valueOf(memberDto.getMember_id()));
    }
}
