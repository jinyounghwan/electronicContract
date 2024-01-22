package com.samsung.framework.controller.account;

import com.samsung.framework.common.enums.AccountTypeEnum;
import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.domain.account.Account;
import com.samsung.framework.domain.account.LoginRequest;
import com.samsung.framework.domain.account.SignUpRequest;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.service.account.AccountService;
import com.samsung.framework.service.menu.MenuService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.PagingVO;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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
    @ModelAttribute("dateRangeSelect")
    public List<SearchVO> searchDateRangeOptionList() {
        SearchVO list = new SearchVO();
        return list.getSearchDateRangeOptionList();
    }

    // [검색옵션] 키워드
    @ModelAttribute("keywordTypeSelect")
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

        accountSearchVO.setPaging(new Paging());

        mv.addObject("list",new ArrayList<>());
        mv.setViewName("account/employeeList");

        SearchVO searchVO = new SearchVO();
        searchVO.setPaging(new Paging());

        Paging paging = ObjectHandlingUtil.pagingOperatorBySearch(searchVO, totalCount);
        log.info("paging CurrentPage : {}",paging.getCurrentPage());
        mv.addObject("paging", paging);
        mv.addObject("search", new AccountSearchVO());
        return mv;
    }

    @GetMapping("/admin")
    public ModelAndView getAdminAccount(ModelAndView mv){
        AccountSearchVO accountSearchVO = AccountSearchVO.builder()
                    .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN))
                    .build();
        int totalCount = accountService.totalCount(accountSearchVO);
        mv.addObject("totalCount", totalCount);

        accountSearchVO.setPaging(new Paging());

        mv.addObject("list",new ArrayList<>());
        mv.setViewName("account/adminList");

        SearchVO searchVO = new SearchVO();
        searchVO.setPaging(new Paging());

        Paging paging = ObjectHandlingUtil.pagingOperatorBySearch(searchVO, totalCount);

        mv.addObject("paging", paging);
        mv.addObject("search", new AccountSearchVO());

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

    @GetMapping("/employee/detail/{userId}")
    public ModelAndView getEmployeeDetail(ModelAndView mv, @PathVariable("userId")String userId){
        AccountVO account = accountService.getAccountDetail(userId);
        return mv;
    }
    @GetMapping("/admin/detail/{userId}")
    public ModelAndView getAdminDetail(ModelAndView mv, @PathVariable("userId")String userId){
        AccountVO account = accountService.getAccountDetail(userId);
        mv.addObject("account",account);
        mv.setViewName("account/adminDetail");
        return mv;
    }

    @GetMapping("/admin/edit/{userId}")
    public ModelAndView getAdminEdit(ModelAndView mv, @PathVariable("userId")String userId){
        AccountVO account = accountService.getAccountDetail(userId);
        mv.addObject("account",account);
        mv.setViewName("account/adminEdit");
        return mv;
    }

    @PostMapping("/admin/api/update")
    public ResponseEntity updAdminAcct(@RequestBody AccountVO account){
        Map<String, Object> result = accountService.updAdminAcct(account);

        return ResponseEntity.ok(result);
    }
    @PostMapping("/employee/getList")
    public String getEmployeeList(Model model, @RequestBody SearchVO search){
        Paging pagingVO = Paging.builder()
                .currentPage(search.getPaging().getCurrentPage())
                .displayRow(search.getPaging().getDisplayRow())
                .totalCount(search.getPaging().getTotalCount())
                .build();

        AccountSearchVO accountSearchVO =AccountSearchVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.EMPLOYEE))
                .build();
        accountSearchVO.setPaging(pagingVO);

        // total
        int totalCount = accountService.totalCount(accountSearchVO);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("paging", pagingVO);

        // list
        List<AccountVO> list = accountService.getAccountList(accountSearchVO);
        model.addAttribute("list",list);
        model.addAttribute("search", accountSearchVO);
        return "account/employeeList :: #content-wrapper";
    }

    @PostMapping("/admin/getList")
    public String getAdminList(Model model, @RequestBody SearchVO search){
        Paging pagingVO = Paging.builder()
                .currentPage(search.getPaging().getCurrentPage())
                .displayRow(search.getPaging().getDisplayRow())
                .totalCount(search.getPaging().getTotalCount())
                .build();

        AccountSearchVO accountSearchVO =AccountSearchVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN))
                .build();
        accountSearchVO.setPaging(pagingVO);

        // total
        int totalCount = accountService.totalCount(accountSearchVO);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("paging", pagingVO);

        // list
        List<AccountVO> list = accountService.getAccountList(accountSearchVO);
        model.addAttribute("list",list);
        model.addAttribute("search", accountSearchVO);
        return "account/adminList :: #content-wrapper";
    }

}
