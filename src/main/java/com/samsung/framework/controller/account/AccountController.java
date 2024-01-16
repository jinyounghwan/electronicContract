package com.samsung.framework.controller.account;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.domain.account.LoginRequest;
import com.samsung.framework.domain.account.SignUpRequest;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.service.account.AccountService;
import com.samsung.framework.service.menu.MenuService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 계정 관련 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/account")
@Controller
public class AccountController {
    private final AccountService accountService;
    private final MenuService menuService;
    private final LogUtil logUtil;

    /**
     * 회원 가입 View
     * @param mv
     * @return
     */
    @GetMapping("/sign-up")
    public ModelAndView signUp(ModelAndView mv){
        mv.addObject("member", new AccountVO());
        mv.setViewName("member/member-sign-up");
        return mv;
    }

    /**
     * 아이디 정보 확인 view
     * @param mv
     * @return
     */
    @GetMapping("/userId-check")
    public ModelAndView userIdCheck(ModelAndView mv, HttpServletRequest request){
        String id = request.getParameter("id");
        log.info("id : {}", id);
        mv.addObject("memberId", id);
        mv.setViewName("member/member-info-check");
        return mv;
    }

    /**
     * 회원 찾기 View
     * @param mv
     * @return
     */
    @GetMapping("/findById")
    public ModelAndView findById(ModelAndView mv){
        mv.addObject("member",new AccountVO());
        mv.setViewName("member/member-find-id");
        return mv;
    }

    /**
     * 회원가입
     * @param signUpRequest
     * @return
     * @throws CustomLoginException
     */
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest) throws CustomLoginException {
        Map<String, Object> resultMap = accountService.signUp(signUpRequest);
        menuService.saveAuthMenu(signUpRequest);

        return ResponseEntity.ok(resultMap);
    }

    /**
     * login view를 가져와 준다.
     * @return
     */
    @GetMapping({"", "/login"})
    public ModelAndView login(ModelAndView mv) {
        mv.addObject("member", new AccountVO());
        mv.setViewName("login/view-login");
        return mv;
    }

    /**
     * login
     * @param loginRequest
     * @param request
     * @return
     * @throws CustomLoginException
     */
    @PostMapping({"", "/login"})
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws CustomLoginException{
        // 유저 이름에 해당하는 유저 정보를 가져온다.
        AccountVO loginInfo = accountService.getLoginInfo(loginRequest);
        HttpSession session = request.getSession();
        if(loginInfo!=null){
            session.setAttribute("loginInfo", loginInfo);
            session.setMaxInactiveInterval(1800);

            var logSaveRequest = LogSaveRequest.builder()
                    .logType(LogTypeEnum.LOGIN)
                    .ipAddress(request.getRemoteAddr() + ":" + request.getRemoteAddr())
                    .createdBy(String.valueOf(loginInfo.getEmpNo()))
                    .build();

            Map<String, LogSaveResponse> resultMap = logUtil.saveLog(logSaveRequest);
            return ResponseEntity.ok().body(resultMap);
        }else {
            session.setAttribute("loginInfo", null);

            throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
        }

    }

    // 로그아웃
    @PostMapping("/logout")
    public ModelAndView logout(ModelAndView mv, HttpSession session){
        session.invalidate();
        mv.setViewName("redirect:/account/login");
        return mv;
    }

}
