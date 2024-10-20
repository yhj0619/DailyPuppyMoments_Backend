package com.example.dpm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.member.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer>{

	 Optional<MemberEntity> findById(int id);

	 Optional<MemberEntity> findByRefreshToken(String refreshToken);
}
