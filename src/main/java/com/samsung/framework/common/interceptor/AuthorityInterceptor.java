package com.samsung.framework.common.interceptor;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.MapKeyStringEnum;
import com.samsung.framework.common.enums.RequestTypeEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.JwtUtil;
import com.samsung.framework.mapper.authority.AuthorityMapper;
import com.samsung.framework.service.member.MemberService;
import com.samsung.framework.vo.authority.MenuAuthorityVO;
import com.samsung.framework.vo.common.TokenVO;
import com.samsung.framework.vo.member.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

/**
 * 권한 체크 인터셉터
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final AuthorityMapper authorityMapper;

    private String[] readPrefix = {"paging", "list", "row", "get", "select", "one"};
    private String[] createPrefix = {"insert", "save"};
    private String[] updatePrefix = {"update", "modify"};
    private String[] deletePrefix = {"delete", "remove"};
    private String[] filePrefix = {"download", "fileDownload", "upload", "fileUpload"};
    private RequestTypeEnum action = null;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("[preHandle]: {}", request.getRequestURI());
//
//        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if(!StringUtils.hasText(authorizationHeader)) {
//            authException(ExceptionCodeMsgEnum.INVALID_AUTH.getMsg());
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
//
//        /**
//         * 메뉴 접근 권한 체크
//         */
//        String menuId = request.getRequestURI().split("/")[1];
//        MenuAuthorityVO menuAuthorityVO = authorityMapper.selectMenuAuthority(memberVO.getUserId(), menuId);
//        if(menuAuthorityVO == null) {
//            authException(ExceptionCodeMsgEnum.NO_MENU_AUTHORITY.getMsg());
//        }
//        menuAuthorityVO.authLevelDivider();
//
//        String methodName = ((HandlerMethod) handler).getMethod().getName();
//        boolean hasNoAuthority = Arrays.stream(targetPrefix(methodName)).filter(p -> methodName.startsWith(p)).findAny().isEmpty();
//        if(hasNoAuthority) {
//            authException(ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg());
//        }
//
//        switch(action) {
//            case READ -> {
//                if(menuAuthorityVO.isRead()) {
//                    return true;
//                }
//                authException(ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg());
//            }
//            case CREATE -> {
//                if(menuAuthorityVO.isCreate()) {
//                    return true;
//                }
//                authException(ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg());
//            }
//            case UPDATE -> {
//                if(menuAuthorityVO.isUpdate()) {
//                    return true;
//                }
//                authException(ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg());
//            }
//            case DELETE -> {
//                if(menuAuthorityVO.isDelete()) {
//                    return true;
//                }
//                authException(ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg());
//            }
//            case DOWNLOAD -> {
//                if(menuAuthorityVO.isDownload()) {
//                    return true;
//                }
//                authException(ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg());
//            }
//            default -> authException(ExceptionCodeMsgEnum.NO_AUTHORITY.getMsg());
//        }

        return true;
    }

//    private void authException(String msg) {
//        throw new AccessDeniedException(msg);
//    }

    private String[] targetPrefix(String obj) {
        boolean isRead = !Arrays.stream(readPrefix).filter(p -> obj.startsWith(p)).findAny().isEmpty();
        boolean isCreate = !Arrays.stream(createPrefix).filter(p -> obj.startsWith(p)).findAny().isEmpty();
        boolean isUpdate = !Arrays.stream(updatePrefix).filter(p -> obj.startsWith(p)).findAny().isEmpty();
        boolean isDelete = !Arrays.stream(deletePrefix).filter(p -> obj.startsWith(p)).findAny().isEmpty();
        boolean isFile = !Arrays.stream(filePrefix).filter(p -> obj.startsWith(p)).findAny().isEmpty();

        if(!isRead && !isCreate && !isUpdate && !isDelete && !isFile) {
            throw new ServerErrorException(ExceptionCodeMsgEnum.INVALID_METHOD_NAMING.getMsg(), null);
        }

        if(isRead && !isCreate && !isUpdate && !isDelete) {
            action = RequestTypeEnum.READ;
            return readPrefix;
        } else if(!isRead && isCreate && !isUpdate && !isDelete) {
            action = RequestTypeEnum.CREATE;
            return createPrefix;
        } else if(!isRead && !isCreate && isUpdate && !isDelete) {
            action = RequestTypeEnum.UPDATE;
            return updatePrefix;
        } else if (!isRead && !isCreate && !isUpdate && isDelete) {
            action = RequestTypeEnum.DELETE;
            return deletePrefix;
        } else if (isFile) {
            action = RequestTypeEnum.DOWNLOAD;
            return filePrefix;
        } else {
            return null;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        action = null;
    }
}
