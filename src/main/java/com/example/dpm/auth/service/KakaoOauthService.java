package com.example.dpm.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.dpm.auth.dto.KakaoInfoDto;
import com.example.dpm.member.dto.MemberDto;
import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.service.MemberService;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final MemberService memberService;

    // 카카오Api 호출해서 AccessToken으로 유저정보 가져오기
    public Map<String, Object> getUserAttributesByToken(String accessToken){
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

 // 카카오API에서 가져온 유저정보를 DB에 저장
    public MemberDto getUserProfileByToken(String accessToken) {
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);
        System.out.println("1. accessToken: " + accessToken);
        
        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);
        
        // MemberDto에 추가된 nickname과 profileImage 정보도 포함
        MemberDto memberDto = MemberDto.builder()
                .member_id(kakaoInfoDto.getId())
                .socialId(kakaoInfoDto.getEmail())
                .profile_nickname(kakaoInfoDto.getNickname())  // 수정된 부분
                .profile_image(kakaoInfoDto.getProfileImage())  // 수정된 부분
                .build();
        
        System.out.println("2. memberDto: " + memberDto);
        
        if (memberService.findById(memberDto.getMember_id()) != null) {
            MemberEntity memberEntity = memberService.toEntity(memberDto);
            memberService.update(memberEntity);
            System.out.println("3.UPDATE : memberService.update done");
        } else {
            MemberEntity memberEntity = memberService.toEntity(memberDto);
            memberService.save(memberEntity);
            System.out.println("3.SAVE : memberService.save done");
        }
        
        return memberDto;
    }

}
