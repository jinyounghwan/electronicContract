package com.samsung.framework.mapper.account;

import com.samsung.framework.domain.account.Account;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.search.SearchVO;
import java.util.List;

public interface AccountMapper {
    int memberListCount();
    int memberListSearchCount(SearchVO search);
    List<AccountVO> getMemberList(SearchVO searchObject);
    AccountVO findMemberById(int id);
    int insertMember(AccountVO account);
    AccountVO findMemberByUserName(String userName);
    AccountVO getLoginInfo(String userName);
    int updatePwd(Account member);
    boolean existsByEmpNo(int empNo);
    /**
     * 전체 사용자 조회
     * @return
     */
    List<AccountVO> findAllUsers(SearchVO searchVO);
}
