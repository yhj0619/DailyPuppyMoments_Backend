package com.example.dpm.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dpm.member.MemberEntity;
import com.example.dpm.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	
	public Optional<MemberEntity> findById(int member_id) {
        return memberRepository.findById(member_id);
    }

    public Optional<MemberEntity> findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken);
    }

    public MemberEntity save(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity);
    }

    public MemberEntity update(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity); // save 메서드는 존재하면 업데이트, 없으면 삽입
    }

    public MemberEntity updateRefreshToken(int member_id, String refreshToken) {
        Optional<MemberEntity> userOpt = memberRepository.findById(member_id);
        if (userOpt.isPresent()) {
        	MemberEntity memberEntity = userOpt.get();
        	memberEntity.setRefreshToken(refreshToken);
            return memberRepository.save(memberEntity);
        }
        return null;
    }
}
