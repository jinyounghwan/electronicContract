//package com.samsung.framework.common.utils;
//
//import com.samsung.framework.domain.common.TokenObjectVO;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
///**
// * 토큰 팩토리
// * TODO: IJ
// * 1. 중복 로그인 처리
// * 2. OAuth 구현
// * 3. http-only 속성이 있는 쿠키에 저장 처리
// */
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class TokenFactory {
//
//    private final JwtUtil jwtUtil;
//    private Long accessExpiresAt = 1000L * 60 * 60; // 1 hour
//    private Long refreshExpiresAt = 1000L * 60 * 60 * 24 * 14; // 14 days
//    /**
//     * JWT 생성
//     * @param userName {@link String}
//     * @param password {@link String}
//     * @param auth {@link Set}
//     * @return {@link TokenObjectVO}
//     * @apiNote password 매개변수는 현재 사용 X
//     */
//    public TokenObjectVO createJWT(String userName, String password, String auth) {
//        TokenObjectVO tokenObjectVO = jwtUtil.createTokenObject(userName, accessExpiresAt, refreshExpiresAt, auth);
//        return tokenObjectVO;
//    }
//}
