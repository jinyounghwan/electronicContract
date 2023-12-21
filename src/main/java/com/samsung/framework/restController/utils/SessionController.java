/*
package com.samsung.framework.restController.utils;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.SessionFactory;
import com.samsung.framework.restController.common.ParentController;
import com.member.domain.framework.samsung.Member;
import com.common.vo.framework.samsung.ResultStatusVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/session")
public class SessionController extends ParentController {

    @Deprecated
    @GetMapping({"", "/"})
    public ResponseEntity createSession(HttpServletRequest request, HttpServletResponse response) {
        Member loginInfo = Member.builder()
                .memberId("ijzone")
                .memberName("이익주")
                .build();
        // TODO: IJ 사용자 로그인 정보 조회 로직 추가
        getCommonUtil().getSessionFactory().createSession(request, loginInfo);
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @GetMapping("/expire")
    public ResponseEntity expireSession(HttpServletRequest request, HttpServletResponse response) {
        getCommonUtil().getSessionFactory().removeSession(request);
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @GetMapping("/login-test")
    public ResponseEntity sessionLogin(HttpServletRequest request, HttpSession session) {
        ResultStatusVO resultStatusVO = new ResultStatusVO();
        List<String> mapKeyList = new ArrayList<>();
        Map<String, Object> resultMap;
        Member loginUserInfo = null;

        Cookie cookie = getCommonUtil().getCookieFactory().findCookie(request, SessionFactory.SessionEnum.SESSION_ID.getSessionKey());
        if(cookie == null) {
            resultStatusVO = new ResultStatusVO(ResultCodeMsgEnum.NO_COOKIE.getCode(), ResultCodeMsgEnum.NO_COOKIE.getMsg());
        }else {
            mapKeyList.add(MapKeyStringEnum.MEMBER.getKeyString());
            loginUserInfo = getCommonUtil().getSessionFactory().getLoginUserInfo(request);

            if(loginUserInfo == null) resultStatusVO = new ResultStatusVO(ResultCodeMsgEnum.NOT_LOGGED_IN.getCode(), ResultCodeMsgEnum.NOT_LOGGED_IN.getMsg());
        }

        resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, loginUserInfo);
        return ResponseEntity.ok(resultMap);
    }

}
*/
