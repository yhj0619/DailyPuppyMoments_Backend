package com.example.dpm.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dpm.auth.exception.CustomException;
import com.example.dpm.auth.exception.ErrorCode;
import com.example.dpm.dto.MemberDto;
import com.example.dpm.model.MemberEntity;
import com.example.dpm.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	
	// MemberEntity를 DTO로 변환
    private MemberDto toDto(MemberEntity memberEntity) {
        return new MemberDto(
            memberEntity.getMember_id(),
            memberEntity.getSocialId(),
            memberEntity.getNickname(),
            memberEntity.getProfile_image(),
            memberEntity.getRefreshToken()
        );
    }

    // memberId로 찾은 후 DTO로 변환
    public MemberDto findById(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
        
        return toDto(memberEntity);
    }

    public Optional<MemberEntity> findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

    public MemberEntity save(MemberEntity memberEntity) {
    	System.out.println("###################################");
        return memberRepository.save(memberEntity);
    }

    public MemberEntity update(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity); // save 메서드는 존재하면 업데이트, 없으면 삽입
    }

    public void updateRefreshToken(Long memberId, String refreshToken) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
        memberEntity.setRefreshToken(refreshToken); // refreshToken 필드에 값 설정
        memberRepository.save(memberEntity); // 변경된 엔티티 저장
    }
    
    // Optional<MemberEntity>를 MemberDto로 변환하는 메서드 추가
    public MemberDto getMemberDtoFromRefreshToken(String refreshToken) {
        Optional<MemberEntity> memberEntityOpt = findByRefreshToken(refreshToken);
        return memberEntityOpt.map(this::toDto).orElse(null);
    }
    
 // MemberDto를 MemberEntity로 변환하는 메서드 추가
    public MemberEntity toEntity(MemberDto memberDto) {
        return MemberEntity.builder()
                .member_id(memberDto.getMember_id())
                .socialId(memberDto.getSocialId())
                // 필요한 필드 추가
                .build();
    }
}
