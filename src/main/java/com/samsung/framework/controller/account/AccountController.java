package com.samsung.framework.controller.account;

import com.samsung.framework.common.enums.AccountTypeEnum;
import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.domain.account.LoginRequest;
import com.samsung.framework.domain.account.PwdChangeRequest;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.log.LogMapper;
import com.samsung.framework.service.account.AccountService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 계정 관련 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/account")
@Controller
public class AccountController {
    private final AccountService accountService;
    private final LogUtil logUtil;
    private final LogMapper logMapper;
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
        return list.getAcctSearchKeywordTypeOptionList();
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
     * 임직원 계정 관리 View
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
     * login view
     * @return
     */
    @GetMapping({"", "/login"})
    public ModelAndView login(HttpServletRequest request, ModelAndView mv) {
        log.info("get login > "  + request.getSession());
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        log.info("get account > "  + account);
        if (account != null) {
            mv.setViewName("redirect:/contract/progress");
            return mv;
        }

        mv.addObject("member", new AccountVO());
        mv.setViewName("account/login");

        log.info("login mv >> " + mv);
        return mv;
    }

    /**
     * TODO: login시 회원가입 값 세팅 수정 필요
     * @param loginRequest
     * @param request
     * @return
     * @throws CustomLoginException
     */
    @PostMapping({"", "/login"})
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws CustomLoginException, NoSuchAlgorithmException {

        // 유저 이름에 해당하는 유저 정보를 가져온다.
        AccountVO loginInfo = accountService.getLoginInfo(loginRequest);

        log.info("로그인" + loginInfo);

        if(loginInfo.getAccountType().equals(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN))){
            loginInfo.setAdminId(loginInfo.getUserId());
        }
        HttpSession session = request.getSession();
        if(loginInfo!=null){
            session.setAttribute("loginInfo", loginInfo);
            session.setMaxInactiveInterval(1800);

            var logSaveRequest = LogSaveRequest.builder()
                    .logType(LogTypeEnum.LOGIN)
                    .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                    .createdBy(String.valueOf(loginRequest.getUserId()))
                    .build();

            Map<String, LogSaveResponse> resultMap = logUtil.saveLog(logSaveRequest);

            // 메일 api 보내고 db에 인증키 저장
            loginMail(loginRequest);

            return ResponseEntity.ok().body(resultMap);
        }else {
            log.info("로그인 else >> ?? ");
            session.setAttribute("loginInfo", null);

            throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
        }

    }

    // 로그인 2차인증 Mail 보내고 db 저장
    @PostMapping({"", "/loginMail"})
    public ResponseEntity loginMail(@RequestBody LoginRequest loginRequest) throws CustomLoginException, NoSuchAlgorithmException {

        log.info("loginRequest>> "  + loginRequest.getUserId());
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        log.info("랜덤 숫자 >> " + randomNumber);

        //랜덤 인증키 Set
        AccountVO accountVO = new AccountVO();
        accountVO.setAuthority(randomNumber);
        accountVO.setEmpNo(loginRequest.getUserId());

        // 1.
        // Knox Mail api 로직 추가해야함 ( 랜덤숫자 / 로그인아이디-메일 실어서 보내야함 )

        // 2. api 보낸 후 db에 저장하는 로직 추가해야함

        // 아이디 받아서 update where에 조건 걸어서 랜덤키 추가
        accountService.updateAuth(accountVO);

        return ResponseEntity.ok().body(randomNumber);
    }

    // 인증키 입력 후 화면에서 인증키 받아와서 select해서 매칭
    @PostMapping("/loginAuth")
    public ResponseEntity loginAuthenticationKey(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws CustomLoginException, NoSuchAlgorithmException {

        // 유저 이름에 해당하는 유저 정보를 가져온다.
        AccountVO loginInfo = accountService.getLoginInfo(loginRequest);

        if(loginInfo.getAccountType().equals(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN))){
            loginInfo.setAdminId(loginInfo.getUserId());
        }
        HttpSession session = request.getSession();
        if(loginInfo!=null){
            session.setAttribute("loginInfo", loginInfo);
            session.setMaxInactiveInterval(1800);

            var logSaveRequest = LogSaveRequest.builder()
                    .logType(LogTypeEnum.LOGIN)
                    .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                    .createdBy(String.valueOf(loginRequest.getUserId()))
                    .build();

            Map<String, LogSaveResponse> resultMap = logUtil.saveLog(logSaveRequest);

            // user테이블에서 아이디로 select해서 받아온 키값
            AccountVO accountVO =  accountService.getAuthInfo(loginRequest);

            // 화면에서 받아온 값
            int selAuth = Integer.parseInt(loginRequest.getAuthority());

            Map<String,Object> result = new HashMap<>();

            if(accountVO.getAuthority() == selAuth){

                result.put("Code" , ExceptionCodeMsgEnum.MATCH_AUTH.getCode());
                result.put("Msg" , ExceptionCodeMsgEnum.MATCH_AUTH.getMsg());
                log.info("값이 같다 ");
            }else{
                result.put("Code" , ExceptionCodeMsgEnum.NO_MATCH_AUTH.getCode());
                result.put("Msg" , ExceptionCodeMsgEnum.NO_MATCH_AUTH.getMsg());
                log.info("값이 다르다 ");
            }
            // 3. 일치합니다 다릅니다 ~ 하고 리턴하기전에 테이블에 들어가있는 인증키 delete
            log.info("resultMap >> " +resultMap);
            return ResponseEntity.ok().body(result);
        }else {
            session.setAttribute("loginInfo", null);

            throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
        }

    }

    @PostMapping("/countDelete")
    public ResponseEntity countDelete(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws CustomLoginException, NoSuchAlgorithmException {
        Map<String,Object> result = new HashMap<>();
        result.put("test" , "test");
        log.info("countDelete 완료 >> " + loginRequest.getUserId());

        accountService.countDelete(loginRequest);
        // 삭제로직 만들어야함
        return ResponseEntity.ok().body(result);
    }
    // 로그아웃
    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView mv, HttpSession session){
        session.invalidate();
        mv.setViewName("redirect:/account/login");
        return mv;
    }

    @GetMapping("/employee/detail/{userId}")
    public ModelAndView getEmployeeDetail(ModelAndView mv, @PathVariable("userId")String userId) throws UnsupportedEncodingException {
        AccountVO vo = AccountVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.EMPLOYEE))
                .userId(userId)
                .build();
        AccountVO account = accountService.getAccountDetail(vo);
        mv.addObject("account", account);
        mv.setViewName("account/employeeDetail");
        return mv;
    }
    @GetMapping("/admin/detail/{userId}")
    public ModelAndView getAdminDetail(ModelAndView mv, @PathVariable("userId")String userId) throws UnsupportedEncodingException {
        AccountVO vo = AccountVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN))
                .userId(userId)
                .build();
        AccountVO account = accountService.getAccountDetail(vo);
        mv.addObject("account",account);
        mv.setViewName("account/adminDetail");
        return mv;
    }

    @GetMapping("/admin/edit/{userId}")
    public ModelAndView getAdminEdit(ModelAndView mv, @PathVariable("userId")String userId) throws UnsupportedEncodingException {
        AccountVO vo = AccountVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN))
                .userId(userId)
                .build();
        AccountVO account = accountService.getAccountDetail(vo);
        mv.addObject("account",account);
        mv.setViewName("account/adminEdit");
        return mv;
    }
    @PostMapping("/api/employee/update")
    public ResponseEntity updEmployeeAcct(HttpServletRequest request, @RequestBody AccountVO account) throws NoSuchAlgorithmException {
        Map<String, Object> result = accountService.updEmployeeAcct(request, account);

        return ResponseEntity.ok(result);
    }
    @PostMapping("/api/admin/update")
    public ResponseEntity updAdminAcct(HttpServletRequest request, @RequestBody AccountVO account) throws NoSuchAlgorithmException {
        Map<String, Object> result = accountService.updAdminAcct(request, account);

        return ResponseEntity.ok(result);
    }
    @PostMapping("/employee/getList")
    public String getEmployeeList(Model model, @RequestBody AccountSearchVO search){
        Paging pagingVO = Paging.builder()
                .currentPage(search.getPaging().getCurrentPage())
                .displayRow(search.getPaging().getDisplayRow())
                .totalCount(accountService.totalCount(search))
                .build();

        AccountSearchVO accountSearchVO =AccountSearchVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.EMPLOYEE))
                .build();

        accountSearchVO.setPaging(pagingVO);
        accountSearchVO.setSearchVO(search);

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
    public String getAdminList(Model model, @RequestBody AccountSearchVO search){
        search.setAccountType(AccountTypeEnum.menuCode(AccountTypeEnum.ADMIN));
        Paging pagingVO = Paging.builder()
                .currentPage(search.getPaging().getCurrentPage())
                .displayRow(search.getPaging().getDisplayRow())
                .totalCount(accountService.totalCount(search))
                .build();

        // total
        model.addAttribute("totalCount", pagingVO.getTotalCount());
        model.addAttribute("paging", pagingVO);

        // list
        List<AccountVO> list = accountService.getAccountList(search);
        model.addAttribute("list",list);
        model.addAttribute("search", search);

        return "account/adminList :: #content-wrapper";
    }

    @GetMapping("/employee/edit/{userId}")
    public ModelAndView getEmployeeEdit(ModelAndView mv, @PathVariable("userId")String userId) throws UnsupportedEncodingException {
        AccountVO vo = AccountVO.builder()
                .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.EMPLOYEE))
                .userId(userId)
                .build();
        AccountVO account = accountService.getAccountDetail(vo);
        mv.addObject("account",account);
        mv.setViewName("account/employeeEdit");

        return mv;
    }
    @PostMapping("/api/pwdChange/isExist")
    @ResponseBody
    public Map<String, Object> isExistsPwdChange(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws UnsupportedEncodingException {

        log.info("isExist >>!!");
        Map<String, Object> result = new HashMap<>();
        boolean exists = logMapper.isExistsPasswordChangeLog(loginRequest.getUserId());

        // 관리자 계정, 임직원 계정 체크 로직
        AccountVO accountVO = accountService.getSessionAccount(request);
        boolean adminIdCheck = StringUtil.isNotEmpty(accountVO.getAdminId());

        result.put("exists", exists);
        result.put("adminIdCheck", adminIdCheck);
        
        return result;
    }

    @GetMapping("/pwdChange")
    public ModelAndView getPwdChange(ModelAndView mv){
        mv.setViewName("account/passwordEdit");
        return mv;
    }

    @PostMapping("/api/pwdChange")
    @ResponseBody
    public ResponseEntity updPwd(HttpServletRequest request, @RequestBody PwdChangeRequest pwdChangeRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        HttpSession session = request.getSession();
        AccountVO loginInfo = (AccountVO) session.getAttribute("loginInfo");

        // 관리자 계정, 임직원 계정 체크 로직
        AccountVO accountVO = accountService.getSessionAccount(request);
        boolean adminIdCheck = StringUtil.isEmpty(accountVO.getAdminId());
        Map<String, Object> result = accountService.updPwd(request, pwdChangeRequest, loginInfo);

        result.put("adminIdCheck", adminIdCheck);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/myInfo")
    public ModelAndView getMyInfo(HttpServletRequest request, ModelAndView mv) throws UnsupportedEncodingException {
        mv.setViewName("account/myInfo");
        mv.addObject("account",accountService.getSessionAccount(request));
        return mv;
    }

    @GetMapping("/myInfo/edit")
    public ModelAndView getChangeMyInfo(HttpServletRequest request, ModelAndView mv) throws UnsupportedEncodingException {
        mv.setViewName("account/myInfoEdit");
        mv.addObject("account", accountService.getSessionAccount(request));
        return mv;
    }

}

