package com.samsung.framework.mapper.account;

import com.samsung.framework.domain.account.Account;
import com.samsung.framework.domain.account.LoginRequest;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.view.HistoryVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.user.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountMapper {
    AccountVO getAccountDetail(AccountVO account);
    List<AccountVO> getAccountList(SearchVO search);
    int findAllTotalCount(SearchVO search);
    int memberListCount();
    int memberListSearchCount(SearchVO search);
    List<AccountVO> getMemberList(SearchVO searchObject);
    AccountVO findMemberById(int id);
    int insertMember(AccountVO account);
    AccountVO findMemberByUserName(String userName);
    AccountVO getLoginInfo(String userId);
    boolean existsByEmpNo(int empNo);
    AccountVO myInfo(AccountVO accountVO);
    int updPwd(AccountVO account);
    int updEmployeeAcct(AccountVO account);
    int updAdminAcct(AccountVO account);

    UserVO getUserInfo(@Param(value="empNo") String empNo);

    int updateAuth(AccountVO account);

    int countDelete(String userId);
    AccountVO getAuthInfo(String userId);

    List<HistoryVO> getAdminHistory(AccountVO accountVO);
}
