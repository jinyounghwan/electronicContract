package com.samsung.framework.service.account;


import com.samsung.framework.common.enums.*;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.*;
import com.samsung.framework.domain.account.LoginRequest;
import com.samsung.framework.domain.account.PwdChangeRequest;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.common.SearchObject;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.account.ghr.GhrAccountMapper;
import com.samsung.framework.mapper.log.LogMapper;
import com.samsung.framework.service.menu.MenuService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.account.ghr.GhtAccountVO;
import com.samsung.framework.vo.common.CollectionPagingVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {
    private final ValidationUtil validationUtil;
    private final EncryptionUtil encryptionUtil;
    private final AccountMapper accountMapper;
    private final LogMapper logMapper;
    private final GhrAccountMapper ghrAccountMapper;
    private final MenuService menuService;
    private final LogUtil logUtil;

    /**
     * 회원 총 개수 (임직원, 관리자)
     * @param accountSearchVO {@link AccountSearchVO}
     * @return {@link Integer}
     */
    public int totalCount(AccountSearchVO accountSearchVO) { return accountMapper.findAllTotalCount(accountSearchVO); }

    /**
     * Admin 사번 회원 상세 정보 조회
     * @param
     * @return
     */
    public AccountVO getAccountDetail(AccountVO vo) throws UnsupportedEncodingException {
        AccountVO account = accountMapper.getAccountDetail(vo);
        account.setCreatedAtStr(DateUtil.convertLocalDateTimeToString(account.getCreatedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        account.setUpdatedAtStr(DateUtil.convertLocalDateTimeToString(account.getUpdatedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        account.setLastLoginStr(DateUtil.convertLocalDateTimeToString(account.getLastLogin(), DateUtil.DATETIME_YMDHM_PATTERN));
        account.setPasswordAtStr(DateUtil.convertLocalDateTimeToString(account.getPasswordAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        getFirstNameLastName(account);

        return account;
    }

    /**
     * 회원 목록 조회
     * @param accountSearchVO {@link AccountSearchVO}
     * @return 회원 목록 반환
     */
    public List<AccountVO> getAccountList(AccountSearchVO accountSearchVO){

        List<AccountVO> list = accountMapper.getAccountList(accountSearchVO);
        list.forEach(data -> {
            data.setCreatedAtStr(DateUtil.convertLocalDateTimeToString(data.getCreatedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
            data.setUpdatedAtStr(DateUtil.convertLocalDateTimeToString(data.getUpdatedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
            data.setLastLoginStr(DateUtil.convertLocalDateTimeToString(data.getLastLogin(), DateUtil.DATETIME_YMDHM_PATTERN));
            data.setPasswordAtStr(DateUtil.convertLocalDateTimeToString(data.getPasswordAt(), DateUtil.DATETIME_YMDHM_PATTERN));

            data.setFirstName(data.getName());
            data.setLastName("");
            int index = data.getName().indexOf(" ");
            if (index != -1) {
                try {
                    String lastName = StringUtil.getSubstring(data.getName(), 0, index);
                    String firstName = StringUtil.getSubstring(data.getName(), index);

                    data.setFirstName(firstName);
                    data.setLastName(lastName);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        return list;
    }

    /**
     * 멤버 목록 조회
     * @param searchObject {@link SearchObject}
     * @return {@link CollectionPagingVO}
     */
    public CollectionPagingVO getMemberList(SearchObject searchObject) {
        int totalCount = accountMapper.memberListCount();
        CollectionPagingVO collectionPagingVO = null;
        if(totalCount > 0) {
            int totalSearchCount = accountMapper.memberListSearchCount(searchObject.getSearch());
            Paging paging = ObjectHandlingUtil.pagingOperator(searchObject, totalSearchCount);
            SearchVO searchVO = new SearchVO();
            searchVO.setPaging(paging);
            List<AccountVO> memberList = accountMapper.getMemberList(searchVO);
            collectionPagingVO = CollectionPagingVO.builder()
                    .objects(memberList)
                    .paging(paging)
                    .build();
        }else {
            collectionPagingVO = CollectionPagingVO.builder()
                    .objects(new ArrayList<>())
                    .paging(Paging.builder().currentPage(1).build())
                    .build();
        }

        return collectionPagingVO;
    }

    /**
     * 멤버 상세 조회
     * @param id int {@link Integer}
     * @return {@link AccountVO}
     */
    public AccountVO findMemberById(int id){
        AccountVO AccountVO = accountMapper.findMemberById(id);

        if(AccountVO == null) {
            return null;
        }

        AccountVO target = AccountVO.builder()
                .empNo(AccountVO.getEmpNo())
                .userId(AccountVO.getUserId())
                .deptCode(AccountVO.getDeptCode())
                .name(AccountVO.getName())
                .email(AccountVO.getEmail())
                .build();

        return target;
    }

    /**
     * 멤버 상세 조회
     * @param userName {@link String}
     * @return {@link AccountVO}
     */
    public AccountVO findMemberByUserName(String userName) {
        return accountMapper.findMemberByUserName(userName);
    }

    /**
     * 로그인 정보 조회
     * @param loginRequest {@link LoginRequest}
     * @return {@link AccountVO}
     */
    public AccountVO getLoginInfo(@Valid LoginRequest loginRequest) throws CustomLoginException, NoSuchAlgorithmException {
        AccountVO vo = null;

        // admindId 체크
        AccountVO target = accountMapper.getLoginInfo(loginRequest.getUserId());

        if(StringUtil.isEmpty(target)) {
            GhtAccountVO ghrAccount = ghrAccountMapper.getGhrInfo(Integer.parseInt(loginRequest.getUserId()));
            
            // ghrAccount 체크
            if(StringUtil.isEmpty(ghrAccount)){
                throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
            }

             target = AccountVO.builder()
                    .empNo(String.valueOf(ghrAccount.getEmpNo()))
                    .userPw(encryptionUtil.encrypt(String.valueOf(ghrAccount.getEmpNo()))) // 초기 비밀번호 세팅은 사번으로
                    .deptCode(ghrAccount.getDepartmentCode())
                    .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.EMPLOYEE))
                    .email("Test@samsung.com")
                    .name(ghrAccount.getName())
                    .position(PositionEnum.menuCode(PositionEnum.valueOf(ghrAccount.getLevel().toUpperCase().replace(" ","_"))))
                    .resignedAt(ghrAccount.getResignationDate())
                    .phone("0100000000")
                    .employedAt(ghrAccount.getHireDate())
                    .build();
            getAccountConverDate(target);
            int inserted = accountMapper.insertMember(target);
            menuService.saveAuthMenu(target);

            if(inserted < 1){
                throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
            }
        }

        if (encryptionUtil.encrypt(loginRequest.getPassword()).equals(target.getUserPw())) {
            return AccountVO.builder()
                    .userId(loginRequest.getUserId())
                    .empNo(target.getEmpNo())
                    .deptCode(target.getDeptCode())
                    .adminId(target.getAdminId())
                    .name(target.getName())
                    .accountType(target.getAccountType())
                    .position(target.getPosition())
                    .email(target.getEmail())
                    .phone(target.getPhone())
                    .build();
        }else {
            try {
                throw new CustomLoginException(ExceptionCodeMsgEnum.INVALID_PASSWORD.getCode(), ExceptionCodeMsgEnum.INVALID_PASSWORD.getMsg());
            } catch (CustomLoginException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * 임직원 계정 변경
     * @param account
     * @return
     */
    public Map<String, Object> updEmployeeAcct(HttpServletRequest request, @Valid AccountVO account) throws NoSuchAlgorithmException {
        var result = new HashMap<String, Object>();

        if(validationUtil.parameterValidator(account, AccountVO.class)){
            AccountVO target = AccountVO.builder()
                    .empNo(account.getUserId())
                    .email(account.getEmail())
                    .userPw(encryptionUtil.encrypt(account.getPassword()))
                    .build();

            result.put("code", 200);

            int update = accountMapper.updEmployeeAcct(target);
            if(update<1){
                result.put("code", 204);
                result.put("message", "수정할 대상이 없습니다.");
                return result;
            }
             result.put("message", "수정 되었습니다.");
            //로그 저장
             var logSaveRequest = LogSaveRequest.builder()
                    .logType(LogTypeEnum.PASSWORD_CHANGE)
                     .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                    .createdBy(String.valueOf(account.getUserId()))
                    .build();

             logUtil.saveLog(logSaveRequest);
             return result;
        }
        result.put("code", 400);
        result.put("message", "비밀번호는 영어와 숫자 포함해서 8~16자리 이내로 입력해주세요.");

        return result;
    }

    /**
     * 관리자 계정 변경
     * @param account
     * @return
     */
    public Map<String, Object> updAdminAcct(HttpServletRequest request, @Valid AccountVO account) throws NoSuchAlgorithmException {
        var result = new HashMap<String, Object>();

        if(validationUtil.parameterValidator(account, AccountVO.class)){
            AccountVO target = AccountVO.builder()
                    .name(account.getName())
                    .adminId(account.getUserId())
                    .userPw(encryptionUtil.encrypt(account.getPassword()))
                    .build();

            result.put("code", 200);

            int update = accountMapper.updAdminAcct(target);
            if(update<1){
                result.put("code", 204);
                result.put("message", "수정할 대상이 없습니다.");
                return result;
            }
            //로그 저장
            var logSaveRequest = LogSaveRequest.builder()
                    .logType(LogTypeEnum.PASSWORD_CHANGE)
                    .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                    .createdBy(String.valueOf(account.getUserId()))
                    .build();
            logUtil.saveLog(logSaveRequest);

            result.put("message", "수정 되었습니다.");

            return result;
        }
        result.put("code", 400);
        result.put("message", "비밀번호는 영어와 숫자 포함해서 8~16자리 이내로 입력해주세요.");

        return result;
    }

    public Map<String, Object> updPwd(HttpServletRequest request,@Valid PwdChangeRequest pwdChangeRequest, AccountVO accountVO) throws NoSuchAlgorithmException {
        var result = new HashMap<String, Object>();
        if(validationUtil.parameterValidator(pwdChangeRequest, PwdChangeRequest.class)){
            AccountVO account = AccountVO.builder()
                    .userPw(encryptionUtil.encrypt(pwdChangeRequest.getPassword()))
                    .email(pwdChangeRequest.getEmail())
                    .userId(accountVO.getUserId())
                    .empNo(accountVO.getEmpNo())
                    .build();

            int update = accountMapper.updPwd(account);
            if(update < 1){
                result.put(MapKeyStringEnum.CODE.getKeyString(), 204);
                result.put(MapKeyStringEnum.MESSAGE.getKeyString(), "수정할 대상이 없습니다.");
            }else {
                result.put(MapKeyStringEnum.CODE.getKeyString(), 200);
                result.put(MapKeyStringEnum.MESSAGE.getKeyString(), "수정 되었습니다.");

                HttpSession session = request.getSession();
                AccountVO loginInfo = (AccountVO) session.getAttribute("loginInfo");

                if (loginInfo != null) {
                    loginInfo.setEmail(account.getEmail()); // 이메일 속성만 수정
                    session.setAttribute("loginInfo", loginInfo); // 수정된 객체를 세션에 다시 저장
                }


                //로그 저장
                var logSaveRequest = LogSaveRequest.builder()
                        .logType(LogTypeEnum.PASSWORD_CHANGE)
                        .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                        .createdBy(String.valueOf(account.getUserId()))
                        .build();
                logUtil.saveLog(logSaveRequest);
            }

            return result;
        }
        result.put(MapKeyStringEnum.CODE.getKeyString(),400);
        result.put(MapKeyStringEnum.MESSAGE.getKeyString(), "비밀번호는 영어와 숫자 포함해서 8~16자리 이내로 입력해주세요.");
        return result;
    }

    public void getAccountConverDate(AccountVO account){
        account.setEmployedAtStr(DateUtil.convertLocalDateTimeToString(account.getEmployedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        account.setResignedAtStr(DateUtil.convertLocalDateTimeToString(account.getResignedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
    }
    public void getFirstNameLastName(AccountVO account) throws UnsupportedEncodingException {
        account.setFirstName(account.getName());
        account.setLastName("");
        int index =account.getName().indexOf(" ");
        if(index > 0){
            String lastName = StringUtil.getSubstring(account.getName(),0,index);
            String firstName = StringUtil.getSubstring(account.getName(), index);
            account.setFirstName(firstName);
            account.setLastName(lastName);
        }
    }

    public AccountVO getSessionAccount(HttpServletRequest request) throws UnsupportedEncodingException {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        getFirstNameLastName(account);
        return account;
    }

    // 랜덤 인증키 6자리 저장 로직
    public void updateAuth(AccountVO account) throws CustomLoginException, NoSuchAlgorithmException {
        log.info("인증키 upd in >>" + account);
        accountMapper.updateAuth(account);
    }

    public AccountVO getAuthInfo(@Valid LoginRequest loginRequest) throws CustomLoginException, NoSuchAlgorithmException {
        AccountVO target = accountMapper.getAuthInfo(loginRequest.getUserId());
        return target;
    }

    // 타이머 종료시 인증암호 삭제로직
    public void countDelete(LoginRequest loginRequest){
        accountMapper.countDelete(loginRequest.getUserId());
    }
}
