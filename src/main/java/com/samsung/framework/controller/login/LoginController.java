package com.samsung.framework.controller.login;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.domain.member.LoginRequest;
import com.samsung.framework.service.member.MemberService;
import com.samsung.framework.vo.member.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController{
    private final MemberService memberService;
    /**
     * login view를 가져와 준다.
     * @return
     */
    @GetMapping({"", "/"})
    public ModelAndView login(ModelAndView mv) {
        mv.addObject("member", new MemberVO());
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
    public ResponseEntity login(@RequestBody LoginRequest loginRequest,HttpServletRequest request) throws CustomLoginException{
        // 유저 이름에 해당하는 유저 정보를 가져온다.
        MemberVO loginInfo = memberService.getLoginInfo(loginRequest);
        HttpSession session = request.getSession();
        if(loginInfo!=null){
            session.setAttribute("loginInfo", loginInfo);
            session.setMaxInactiveInterval(1800);

            return new ResponseEntity(loginInfo, HttpStatus.OK);
        }else {
            session.setAttribute("loginInfo", null);

            throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
        }

    }
}
