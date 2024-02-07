package com.samsung.framework.common.interceptor;

import com.samsung.framework.common.enums.AccountTypeEnum;
import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.vo.account.AccountVO;
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
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[preHandle]: {}", request.getRequestURI());
        HttpSession session = null;
        ModelAndView mv = getModelAndView("common/messageRedirect", "로그인 후 사용해 주세요.", "/account/login");

        try{
            session = request.getSession();

            if (session != null) {
                AccountVO loginInfo = (AccountVO) session.getAttribute("loginInfo");
                if(loginInfo != null) {
                    if(loginInfo.getAccountType().equals(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN))) {
                        return true;
                    }else {
                        mv = getModelAndView("common/messageRedirect", ExceptionCodeMsgEnum.NO_MENU_AUTHORITY.getMsg(), "/contract/sign/wait");
                    }
                }
            }

            if(request.getRequestURI().equals("/")) return true;

            throw new ModelAndViewDefiningException(mv);
        } catch (Exception e) {
            throw new ModelAndViewDefiningException(mv);
        }

    }

    private static ModelAndView getModelAndView(String viewName, String msgCode, String redirectUrl) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        modelAndView.addObject("msgCode", msgCode);
        modelAndView.addObject("redirectUrl", redirectUrl);
        return modelAndView;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Callee ==> {}", handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
