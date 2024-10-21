package com.example.dpm.member.service;

import org.springframework.stereotype.Service;

import com.example.dpm.exception.CustomException;
import com.example.dpm.exception.ErrorCode;
import com.example.dpm.member.dto.MemberDto;
import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    
    // MemberEntity를 DTO로 변환
    private MemberDto toDto(MemberEntity memberEntity) {
        return MemberDto.builder()
            .member_id(memberEntity.getMember_id())
            .socialId(memberEntity.getSocialId())
            .profile_nickname(memberEntity.getNickname()) // 필드명 수정
            .profile_image(memberEntity.getProfile_image())
            .refreshToken(memberEntity.getRefreshToken())
            .build();
    }

    // MemberDto를 MemberEntity로 변환
    public MemberEntity toEntity(MemberDto memberDto) {
        return MemberEntity.builder()
            .member_id(memberDto.getMember_id())
            .socialId(memberDto.getSocialId())
            .nickname(memberDto.getProfile_nickname()) // 필드명 수정
            .profile_image(memberDto.getProfile_image())
            .refreshToken(memberDto.getRefreshToken())
            .build();
    }

    // memberId로 찾은 후 DTO로 변환
    public MemberDto findById(Long memberId) {
        return memberRepository.findById(memberId)
            .map(this::toDto)
            .orElseThrow(() -> {
                System.err.println("#MemberService: Member not found for memberId: " + memberId);
                return new CustomException(ErrorCode.BAD_REQUEST);
            });
    }

    // Optional<MemberEntity>를 MemberDto로 변환하는 메서드
    public MemberDto getMemberDtoFromRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken)
            .map(this::toDto)
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
    }

    @Transactional
    public MemberEntity save(MemberEntity memberEntity) {
        try {
            System.out.println("Saving member: " + memberEntity);
            return memberRepository.save(memberEntity);
        } catch (Exception e) {
            System.err.println("Error saving member: " + e.getMessage());
            throw e; // 예외를 다시 던져서 호출자에게 전달
        }
    }

    // 존재하면 업데이트, 없으면 삽입
    public MemberEntity update(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity); 
    }

    // refreshToken 업데이트 메서드
    @Transactional
    public void updateRefreshToken(Long memberId, String refreshToken) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
        
        memberEntity.setRefreshToken(refreshToken); 
        memberRepository.save(memberEntity); // 변경된 엔티티 저장
    }
}
