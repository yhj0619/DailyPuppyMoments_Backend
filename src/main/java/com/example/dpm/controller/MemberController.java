package com.example.dpm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dpm.auth.exception.CustomException;
import com.example.dpm.auth.exception.ErrorCode;
import com.example.dpm.auth.utils.SecurityUtil;
import com.example.dpm.dto.MemberDto;
import com.example.dpm.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberController {
	private final MemberService memberService;
	
	@GetMapping("/member/{member_id}")
    public MemberDto info() {
        final Long userId = SecurityUtil.getCurrentUserId();
        MemberDto memberDto = memberService.findById(userId);
        if(memberDto == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }
        return memberDto;
    }
}
