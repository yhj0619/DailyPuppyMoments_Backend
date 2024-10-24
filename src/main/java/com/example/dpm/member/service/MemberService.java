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
            .nickname(memberEntity.getNickname()) // 필드명 수정
            .profile_image(memberEntity.getProfile_image())
            .point(memberEntity.getPoint())
            .attendance(memberEntity.isAttendance())
            .is_deleted(memberEntity.is_deleted())
            .refreshToken(memberEntity.getRefreshToken())
            .build();
    }

    // MemberDto를 MemberEntity로 변환
    public MemberEntity toEntity(MemberDto memberDto) {
        return MemberEntity.builder()
            .member_id(memberDto.getMember_id())
            .socialId(memberDto.getSocialId())
            .nickname(memberDto.getNickname()) 
            .profile_image(memberDto.getProfile_image())
            .point(memberDto.getPoint())
            .attendance(memberDto.isAttendance())
            .is_deleted(memberDto.is_deleted())
            .refreshToken(memberDto.getRefreshToken())
            .build();
    }

//    // memberId로 찾은 후 DTO로 변환
//    public MemberDto findById(Long memberId) {
//        return memberRepository.findById(memberId)
//            .map(this::toDto)
//            .orElseThrow(() -> {
//                System.err.println("#MemberService: Member not found for memberId: " + memberId);
//                return new CustomException(ErrorCode.BAD_REQUEST);
//            });
//    }

    //is_deleted() 값 확인
    public MemberDto findById(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .filter(member -> !member.is_deleted()) // 탈퇴 여부 필터링 확인
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
        
        return toDto(memberEntity);
    }
    
    // refresh token으로 Optional<MemberEntity>를 MemberDto로 변환하는 메서드
    public MemberDto getMemberDtoFromRefreshToken(String refreshToken) {
        System.out.println("#MemberService Received refresh token: " + refreshToken); // 리프레시 토큰을 출력

        return memberRepository.findByRefreshToken(refreshToken)
            .map(memberEntity -> {
                System.out.println("#MemberService Found member entity for refresh token: " + memberEntity); // 멤버 엔티티가 발견된 경우 출력
                return toDto(memberEntity);
            })
            .orElseThrow(() -> {
                System.out.println("#MemberService No member found for refresh token: " + refreshToken); // 멤버가 발견되지 않은 경우 출력
                return new CustomException(ErrorCode.BAD_REQUEST);
            });
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
    
    public void saveTokens(Long member_id, String accessToken, String refreshToken) {
        Optional<MemberEntity> memberOptional = memberRepository.findById(member_id);

        MemberEntity memberEntity;
        if (memberOptional.isPresent()) {
            memberEntity = memberOptional.get();
        } else {
            memberEntity = MemberEntity.builder()
                .member_id(member_id)
                .build();
        }
    }
    
    @Transactional
    public void deleteMember(Long memberId) {
        // 회원을 찾아옴
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
        
        // 탈퇴 처리: nickname을 "탈퇴회원"으로 변경하고 나머지 필드를 초기화
        memberEntity.setNickname("탈퇴회원");
        memberEntity.setProfile_image(null); // 프로필 이미지 초기화
        memberEntity.setPoint(0); // 포인트 초기화
        memberEntity.setAttendance(false); // 출석 여부 초기화
        memberEntity.setRefreshToken(null); // 리프레시 토큰 초기화
        memberEntity.set_deleted(true); // 탈퇴 여부 설정

        // 변경된 엔티티 저장
        memberRepository.save(memberEntity);
    }


}
