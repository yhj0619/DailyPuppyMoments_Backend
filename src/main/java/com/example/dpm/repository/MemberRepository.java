package com.example.dpm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.model.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

	 Optional<MemberEntity> findById(Long id);

	 Optional<MemberEntity> findByRefreshToken(String refreshToken);
}
