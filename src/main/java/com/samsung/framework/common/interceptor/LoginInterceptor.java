package com.samsung.framework.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

/**
 * 로그인 시 토큰 및 계정 정보 확인용 인터셉터
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[preHandle]: {}", request.getRequestURI());
        HttpSession session = null;
        try{
            session = request.getSession();

            if(session != null && session.getAttribute("loginInfo") != null){
                return true;
            } else {
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("common/messageRedirect");
                modelAndView.addObject("msgCode", "로그인 후 사용해 주세요.");
                modelAndView.addObject("redirectUrl", "/account/login");
                throw new ModelAndViewDefiningException(modelAndView);
            }
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/messageRedirect");
            modelAndView.addObject("msgCode", "로그인 후 사용해 주세요.");
            modelAndView.addObject("redirectUrl", "/account/login");
            throw new ModelAndViewDefiningException(modelAndView);
        }

    }



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Callee ==> {}", handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
