package com.samsung.framework.service.account;


import com.samsung.framework.common.config.JasyptConfig;
import com.samsung.framework.common.enums.AccountTypeEnum;
import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.PositionEnum;
import com.samsung.framework.common.exception.CustomLoginException;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.common.utils.ValidationUtil;
import com.samsung.framework.domain.account.LoginRequest;
import com.samsung.framework.domain.account.SignUpRequest;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.common.SearchObject;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.CollectionPagingVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {
    // TODO: IJ NPE 위험코드 Optional로 변경
    private final ValidationUtil validationUtil;
    private final JasyptConfig jasyptConfig;
    private final AccountMapper accountMapper;


    /**
     * 회원 총 개수 (임직원, 관리자)
     * @param accountSearchVO {@link AccountSearchVO}
     * @return {@link Integer}
     */
    public int totalCount(AccountSearchVO accountSearchVO) { return accountMapper.findAllTotalCount(accountSearchVO); }

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
     * 사용자 등록
     * @param signUpRequest {@link SignUpRequest}
     * @return {@link Map <String,Object> }
     */
    public Map<String,Object> signUp(@Valid SignUpRequest signUpRequest) {
        Map<String, Object> resultMap = new HashMap<>();

        if(validationUtil.parameterValidator(signUpRequest, SignUpRequest.class)){
            // (임시코드) 추후 수정
            if(accountMapper.existsByEmpNo(signUpRequest.getEmpNo())) {
                resultMap.put("result", "이미 존재하는 사번입니다.");
                return resultMap;
            }

            var target = AccountVO.builder()
                    .empNo(signUpRequest.getEmpNo())
                    .deptCode(signUpRequest.getDeptCode())
                    .userPw(jasyptConfig.jasyptEncrypt(signUpRequest.getUserPw()))
                    .name(signUpRequest.getName())
                    .accountType(AccountTypeEnum.menuCode(AccountTypeEnum.EMPLOYEE))
                    .position(PositionEnum.menuCode(PositionEnum.STAFF))
                    .email(signUpRequest.getEmail())
                    .phone(signUpRequest.getPhone())
                    .employedAt(signUpRequest.getEmployedAt())
                    .resignedAt(signUpRequest.getResignedAt())
                    .updatedAt(signUpRequest.getUpdatedAt())
                    .build();
            int inserted = accountMapper.insertMember(target);

            // 정상 insert
            if(inserted > 0) { // TOKEN 추후 제거
                resultMap.put("result", target);
                return resultMap;
            }
        }
        resultMap.put("result", "사용자 등록 오류");
        return resultMap;
    }

    /**
     * 로그인 정보 조회
     * @param loginRequest {@link LoginRequest}
     * @return {@link AccountVO}
     */
    public AccountVO getLoginInfo(@Valid LoginRequest loginRequest) throws CustomLoginException {
        log.info("UserId : {}", loginRequest.getUserId());
        AccountVO target = accountMapper.getLoginInfo(loginRequest.getUserId());

        if(target == null) {
            try {
                throw new CustomLoginException(ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getCode(), ExceptionCodeMsgEnum.ACCOUNT_NOT_EXISTS.getMsg());
            } catch (CustomLoginException e) {
                throw new RuntimeException(e);
            }
        }

        if (loginRequest.getPassword().equals(jasyptConfig.jasyptDecrypt(target.getUserPw()))) {
            return AccountVO.builder()
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

}
