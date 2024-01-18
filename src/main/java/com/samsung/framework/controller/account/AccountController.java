package com.samsung.framework.controller.account;

import com.samsung.framework.common.enums.AccountTypeEnum;
import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.domain.account.LoginRequest;
import com.samsung.framework.domain.account.SignUpRequest;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.service.account.AccountService;
import com.samsung.framework.service.menu.MenuService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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
    
    // [검색옵션] 날짜
    @ModelAttribute("searchDateRangeOptionList")
    public List<SearchVO> searchDateRangeOptionList() {
        SearchVO list = new SearchVO();
        return list.getSearchDateRangeOptionList();
    }

    // [검색옵션] 키워드
    @ModelAttribute("searchKeywordTypeOptionList")
    public List<SearchVO> searchKeywordTypeOptionList() {
        SearchVO list = new SearchVO();
        return list.getSearchKeywordTypeOptionList();
    }

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
     * 임직원 계정 관리 View(임시)
     */
    @GetMapping("/employee")
    public ModelAndView getEmployeeAccountList(ModelAndView mv){
        AccountSearchVO accountSearchVO = AccountSearchVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.EMPLOYEE))
                .build();
        int totalCount = accountService.totalCount(accountSearchVO);
        mv.addObject("totalCount",totalCount);

        BoardSearchVO boardSearchVO = new BoardSearchVO();
        boardSearchVO.setPaging(Paging.builder()
                .currentPage(1)
                .displayRow(10)
                .build());

        List<AccountVO> list = accountService.getAccountList(accountSearchVO);

        mv.setViewName("account/employeeAccountManage");
        mv.addObject("list",list);

        SearchVO searchVO = new SearchVO();
        searchVO.setPaging(Paging.builder()
                .currentPage(1)
                .displayRow(10)
                .build()
        );
        Paging paging = ObjectHandlingUtil.pagingOperatorBySearch(searchVO, totalCount);
        mv.addObject("paging", paging);

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
    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView mv, HttpSession session){
        session.invalidate();
        mv.setViewName("redirect:/account/login");
        return mv;
    }

}
