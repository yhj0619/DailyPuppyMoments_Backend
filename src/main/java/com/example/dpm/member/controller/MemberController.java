//package com.example.dpm.member.controller;
//
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.dpm.auth.service.JwtTokenService;
//import com.example.dpm.auth.service.OauthService;
//import com.example.dpm.auth.utils.SecurityUtil;
//import com.example.dpm.exception.CustomException;
//import com.example.dpm.exception.ErrorCode;
//import com.example.dpm.member.dto.MemberDto;
//import com.example.dpm.member.service.MemberService;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequiredArgsConstructor
//public class MemberController {
//	private final MemberService memberService;
//
//	private final OauthService oauthService;
//	
//	private final JwtTokenService jwtTokenService;
//
//	// 카카오 로그인 API
//	@PostMapping("/login")
//	public ResponseEntity<Map<String, Object>> login(@RequestParam(name = "accessToken") String accessToken,
//			@RequestParam(name = "refreshToken") String refreshToken, HttpServletResponse response) {
//		String jwtToken = oauthService.loginWithKakao(accessToken, refreshToken, response);
//		return ResponseEntity.ok(Map.of("token", jwtToken));
//	}
//
//	// 로그아웃 API
//	@PostMapping("/logout")
//	public ResponseEntity<Map<String, Object>> logout(@RequestParam(name = "accessToken") String accessToken) {
//		Map<String, Object> logoutResponse = oauthService.logout(accessToken);
//		return ResponseEntity.ok(logoutResponse);
//	}
//
////	@GetMapping("/member/{member_id}")
////	public MemberDto info() {
////		final Long userId = SecurityUtil.getCurrentUserId();
////		System.out.println("[[[MemberController]]]현재 로그인된 사용자 ID: " + userId); // 로그 추가
////		MemberDto memberDto = memberService.findById(userId);
////		if (memberDto == null) {
////			throw new CustomException(ErrorCode.NOT_EXIST_USER);
////		}
////		return memberDto;
////	}
//	// 사용자 정보 가져오기 API
////    @GetMapping("/user/profile")
////    public ResponseEntity<MemberDto> getUserProfile(HttpServletRequest request) {
////        // Authorization 헤더에서 JWT 토큰 추출
////        String authorizationHeader = request.getHeader("Authorization");
////        
////        // Bearer 토큰에서 "Bearer " 부분 제거
////        String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ") ?
////                authorizationHeader.substring(7) : null;
////
////        // JWT 토큰에서 사용자 ID 추출
////        Long memberId = jwtTokenService.getMemberIdFromToken(token); // 필요한 경우 이 메서드를 추가해야 함
////
////        // 사용자 정보 가져오기
////        MemberDto memberDto = memberService.findById(memberId);
////        return ResponseEntity.ok(memberDto);
////    }
//	
//
//	// 연결 끊기 API
//	@DeleteMapping("/user/unlink")
//	public ResponseEntity<Map<String, Object>> unlinkUser(@RequestHeader("Authorization") String accessToken) {
//		// Bearer 토큰에서 "Bearer " 부분 제거
//		String token = accessToken.replace("Bearer ", "");
//
//		Map<String, Object> unlinkResponse = oauthService.unlink(token);
//
//		return ResponseEntity.ok(unlinkResponse);
//	}
//}
