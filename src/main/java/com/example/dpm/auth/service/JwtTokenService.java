package com.example.dpm.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.dpm.auth.dto.TokenResponseDto;
import com.example.dpm.exception.CustomException;
import com.example.dpm.exception.ErrorCode;
import com.example.dpm.member.dto.MemberDto;
import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.service.MemberService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class JwtTokenService implements InitializingBean {
    private long accessTokenExpirationInSeconds;
    private long refreshTokenExpirationInSeconds;
    private final String secretKey;
    private static Key key;
    
    @Autowired
    private MemberService memberService;

    public JwtTokenService(
            @Value("${jwt.access.token.expiration.seconds}") long accessTokenExpirationInSeconds,
            @Value("${jwt.refresh.token.expiration.seconds}") long refreshTokenExpirationInSeconds,
            @Value("${jwt.token.secret-key}") String secretKey
    ) {
        this.accessTokenExpirationInSeconds = accessTokenExpirationInSeconds * 1000;
        this.refreshTokenExpirationInSeconds = refreshTokenExpirationInSeconds * 1000;
        this.secretKey = secretKey;
    }

    // 빈 주입받은 후 실행되는 메소드
    @Override
    public void afterPropertiesSet() {
        this.key = getKeyFromBase64EncodedKey(encodeBase64SecretKey(secretKey));
        System.out.println("!!!!!!!!!!!JwtTokenService_key: " + key);
    }


    public String createJWTToken(String accessToken, String refreshToken,String payload){
        return createToken(accessToken, refreshToken,payload, accessTokenExpirationInSeconds);
    }

//    public String createRefreshToken(){
//        byte[] array = new byte[7];
//        new Random().nextBytes(array);
//        String generatedString = new String(array, StandardCharsets.UTF_8);
//        return createToken(generatedString, refreshTokenExpirationInSeconds);
//    }

    public String createToken(String accessToken, String refreshToken,String payload, long expireLength){
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        
        MemberDto memberDto =  memberService.getMemberDtoFromRefreshToken(refreshToken);
                
        Map<String, Object> claims = new HashMap<>();
        claims.put("member_id", memberDto.getMember_id());
        claims.put("socialId", memberDto.getSocialId());
        claims.put("nickname", memberDto.getNickname());
        claims.put("profile_image", memberDto.getProfile_image());  // 필요한 경우 카카오 토큰도 포함
        claims.put("accessToken", accessToken);  // 필요한 경우 카카오 토큰도 포함
        claims.put("refreshToken", refreshToken);  // 필요한 경우 카카오 토큰도 포함
        
        System.out.println("$$$$$$$$$$$$JWTTOken claims: " + claims.toString());

        
        return Jwts.builder()
                .setClaims(claims)  // 사용자 정보 포함
                .setIssuedAt(now)  // 발행 시간
                .setExpiration(validity)  // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)  // 서명 알고리즘 및 비밀키 설정
                .compact();
    }

    public String getPayload(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        }catch (JwtException e){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public boolean validateToken(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);
            
            // 토큰의 만료 여부 확인
            boolean isValid = !claimsJws.getBody().getExpiration().before(new Date());
            System.out.println("##JwtTokenService Token is valid: " + isValid); // 토큰 유효성 출력
            return isValid;
        } catch (ExpiredJwtException e) {
            System.out.println("##JwtTokenService Token has expired: " + e.getMessage()); // 만료된 토큰 예외 출력
            return false;
        } catch (JwtException | IllegalArgumentException exception) {
            System.out.println("##JwtTokenService Invalid token: " + exception.getMessage()); // 유효하지 않은 토큰 예외 출력
            return false;
        }
    }


    private String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

//	public Long getMemberIdFromToken(String token) {
//		// JWT 검증 및 Claims 가져오기
//	    Claims claims = Jwts.parser()
//	            .setSigningKey(secretKey) // 비밀 키
//	            .parseClaimsJws(token)
//	            .getBody();
//
//	    // 사용자 ID 추출
//	    return claims.get("memberId", Long.class); // "memberId"는 JWT 페이로드에 포함된 사용자 ID 키
//	}

//	public String createAccessToken(String valueOf) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
