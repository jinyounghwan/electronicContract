package com.samsung.framework.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 공통적으로 사용되는 각종 유틸 팩토리 모음
 */
@RequiredArgsConstructor
@Component
public class CommonUtil {

//    private final TokenFactory tokenFactory;
    private final SessionFactory sessionFactory;
    private final CookieFactory cookieFactory;

//    public TokenFactory getTokenFactory() {
//        return this.tokenFactory;
//    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public CookieFactory getCookieFactory() {
        return this.cookieFactory;
    }
}
