package com.example.dpm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dpm.dto.MemberDto;
import com.example.dpm.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
	private final MemberService memberService;
	
	@GetMapping("/info")
    public MemberDto info() {
        final long userId = SecurityUtil.getCurrentUserId();
        MemberDto memberDto = memberService.findById(userId);
        if(userDto == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }
        return userDto;
    }
}
