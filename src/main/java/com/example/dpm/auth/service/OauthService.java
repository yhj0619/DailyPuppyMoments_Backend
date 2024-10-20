package com.example.dpm.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    //카카오 로그인
    public String loginWithKakao(String accessToken, HttpServletResponse response) {
    	System.out.println("#OauthService: 또여기????");
        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken);
        System.out.println("#OauthService: access token????" + getTokens(memberDto.getMember_id(), response));
        return getTokens(memberDto.getMember_id(), response);
    }

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
