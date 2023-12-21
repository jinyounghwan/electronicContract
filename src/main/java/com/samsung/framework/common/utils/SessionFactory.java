package com.samsung.framework.common.utils;

import com.samsung.framework.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionFactory {

    @AllArgsConstructor
    @Getter
    public enum SessionEnum {
        SESSION_ID("JSESSIONID"), LOGIN_MEMBER("loginMember");

        private String sessionKey;
    }

    public <T> void createSession(HttpServletRequest request, T obj) {
        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(httpSession.getId(), obj);
        httpSession.setMaxInactiveInterval(1800);
    }

    public void removeSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public Member getLoginUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return null;
        }

        CookieFactory cookieFactory = new CookieFactory();
        Cookie jSessionId = cookieFactory.findCookie(request, SessionEnum.SESSION_ID.getSessionKey());
        if(jSessionId == null) {
            return null;
        }

        Member member = (Member) session.getAttribute(jSessionId.getValue());
        if(member != null) {
            log.info(member.toString());
        }

        return member;
    }
}
