package com.samsung.framework.common.interceptor;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.MapKeyStringEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.service.member.MemberService;
import com.samsung.framework.vo.common.TokenVO;
import com.samsung.framework.vo.member.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 로그인 시 토큰 및 계정 정보 확인용 인터셉터
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LoginInterceptor implements HandlerInterceptor {

//  private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[preHandle]: {}", request.getRequestURI());

//        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if(!StringUtils.hasText(authorizationHeader)) {
//            return false;
//        }
//
//        TokenVO tokenVO = jwtUtil.decode(authorizationHeader.split("Bearer ")[1]);
//        if(tokenVO == null) {
//            throw new InvalidBearerTokenException(ExceptionCodeMsgEnum.INVALID_AUTH.getMsg());
//        }
//        request.setAttribute(MapKeyStringEnum.TOKEN_VO.getKeyString(), tokenVO);
//
//        MemberVO memberVO = memberService.findMemberByUserName(tokenVO.getUserName());
//        if(memberVO == null) {
//            throw new CustomLoginException(ExceptionCodeMsgEnum.LOGIN_REQUIRED.getCode(), ExceptionCodeMsgEnum.LOGIN_REQUIRED.getMsg());
//        }
//        request.setAttribute(MapKeyStringEnum.MEMBER_VO.getKeyString(), memberVO);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Callee ==> {}", handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {}
}
