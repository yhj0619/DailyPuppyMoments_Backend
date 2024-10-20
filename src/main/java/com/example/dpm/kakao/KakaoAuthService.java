//package com.example.dpm.kakao;
//
//import org.springframework.stereotype.Service;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@Service
//public class KakaoAuthService {
//	private final KakaoUserInfo kakaoUserInfo;
//    //private final UserRepository userRepository;
//
//    @Transactional(readOnly = true)
//    public Long isSignedUp(String token) { 
//        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
//        User user = userRepository.findByKeyCode(userInfo.getId().toString()).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
//        return user.getId();
//    }
//}
