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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

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
                        .with("client_id", "72ca8d5c3abeee717446fc97a3749656")
                        .with("redirect_uri", "http://localhost:8080/login/oauth/kakao")
                        .with("code", code))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
    
    public Map<String, Object> refreshAccessToken(String refreshToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        // Refresh Token을 이용한 Access Token 갱신 요청
        Map<String, Object> tokenResponse = webClient.post()
        		.uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", "72ca8d5c3abeee717446fc97a3749656")
                        .with("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return tokenResponse;  // 새로운 Access Token과 Refresh Token을 포함한 응답
    }	


    //카카오 로그인
//    public String loginWithKakao(String accessToken,String refreshToken, HttpServletResponse response) {
//    	System.out.println("#OauthService: 또여기????");
//        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken,refreshToken);
//        System.out.println("#OauthService: access token????" + accessToken);
//        System.out.println("#OauthService: refreshToken????" + refreshToken);
//        return accessToken;
//    }
    
    public String loginWithKakao(String accessToken, String refreshToken, HttpServletResponse response) {
        System.out.println("#OauthService: 또여기????");
        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken, refreshToken);
        System.out.println("#OauthService: access token????" + accessToken);
        System.out.println("#OauthService: refreshToken????" + refreshToken);

        if (memberDto == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        // 자체적인 JWT 토큰 생성
        String JWTToken = jwtTokenService.createJWTToken(accessToken,refreshToken,String.valueOf(memberDto.getMember_id()));
        
        System.out.println("~~~~~~~~~~~~~@@@@@OauthService_JWTToken: " + JWTToken);
        
     // DB에 리프레시 토큰 저장 (선택 사항)
        //memberService.updateRefreshToken(memberDto.getMember_id(), jwtRefreshToken);
        
     // TokenResponseDto 생성
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setAccessToken(accessToken);
        tokenResponseDto.setRefreshToken(refreshToken);
        return JWTToken;  // TokenResponseDto 객체 반환
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
//    public String getTokens(Long id, HttpServletResponse response) {
//        final String accessToken = jwtTokenService.createAccessToken(String.valueOf(id));
//        final String refreshToken = jwtTokenService.createRefreshToken();
//
//        MemberDto memberDto = memberService.findById(id);
//        memberDto.setRefreshToken(refreshToken);  // refresh_token을 memberDto에 설정
//        memberService.updateRefreshToken(memberDto.getMember_id(), refreshToken);  // DB에 업데이트
//
//        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
//        return accessToken;
//    }
//
    // 리프레시 토큰으로 액세스토큰 새로 갱신
//    public String refreshAccessToken(String jwtToken ,String accessToken,String refreshToken) {
//        // 리프레시 토큰 출력
//        System.out.println("##OauthService Received refresh token: " + refreshToken);
//
//        // MemberDto 가져오기
//        MemberDto memberDto = memberService.getMemberDtoFromRefreshToken(refreshToken);
//        if (memberDto == null) {
//            System.out.println("##OauthService Invalid refresh token: Member not found.");
//            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        // JWT 토큰 유효성 검사
//        boolean isValidToken = jwtTokenService.validateToken(refreshToken);
//        System.out.println("##########vOauthService: isValidToken" + isValidToken);
//        if (!isValidToken) {
//            System.out.println("##OauthService Invalid refresh token: Token is not valid.");
//            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        // 액세스 토큰 생성
//        String JWTtoken = jwtTokenService.createJWTToken(accessToken, refreshToken, String.valueOf(memberDto.getMember_id()));
//
//        // 만료된 경우 새 액세스토큰 생성
//        String newAccessToken = jwtTokenService.createAccessToken(String.valueOf(memberDto.getMember_id()));
//        System.out.println("##OauthService New access token created: " + newAccessToken);
//        return newAccessToken;
//    } else {
//        // 액세스 토큰이 아직 유효한 경우
//        System.out.println("##OauthService Access token is still valid.");
//        throw new CustomException(ErrorCode.ACCESS_TOKEN_STILL_VALID);
//    }
//        
//        
//        System.out.println("##OauthService New access token created: " + newAccessToken);
//
//        return newAccessToken;
//    }

}
