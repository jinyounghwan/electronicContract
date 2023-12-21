package com.samsung.framework.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CookieFactory {

    public static final String SESSION_COOKIE_NAME = "JSESSIONID";

    // TODO: IJ ResponseCookie 방식 전환 여부 확인 필요

    /**
     * 쿠키 생성
     * @param response {@link HttpServletResponse}
     * @param domain {@link String} 도메인
     * @param path {@link String} 경로
     * @param key {@link String} 쿠키 키 값
     * @param value {@link String} 쿠키 밸류 값
     * @param httpOnly {@link Boolean}
     * @param secure {@link Boolean}
     * @param maxAge {@link Integer}
     */
    public void setCookie(HttpServletResponse response, String domain, String path, String key, String value, boolean httpOnly, boolean secure, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath(path);
        cookie.setMaxAge(1 * 24 * 60 * 60);
        cookie.setDomain(domain);

        response.addCookie(cookie);
    }

    /**
     * 쿠키 조회
     * @param request {@link HttpServletRequest}
     * @param cookieName {@link String}
     * @return cookie {@link Cookie}
     */
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    /**
     * 쿠키 제거
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param key {@link String} 쿠키 키 값
     */
    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie cookie = this.findCookie(request, key);
        if(cookie != null) {
            cookie.setMaxAge(0);
            cookie.setDomain("localhost");  // TODO: IJ 유효 도메인으로 추후 변경 필요
            cookie.setPath(request.getRequestURI());
            response.addCookie(cookie);
        }
    }

    /**
     * 쿠키 리스트 추출
     * @param request {@link HttpServletRequest}
     * @return list {@link List}
     */
    public List<Cookie> getCookieList(HttpServletRequest request) {
        List<Cookie> list = new ArrayList<Cookie>();
        if(request.getCookies() != null && request.getCookies().length > 0) {
            list = Arrays.asList(request.getCookies());
            list.forEach(c -> log.info("Cookie name: {}, value: {}", String.valueOf(c.getName()), String.valueOf(c.getValue())));
        }

        return list;
    }
}
