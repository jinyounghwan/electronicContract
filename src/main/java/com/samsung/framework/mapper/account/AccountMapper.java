package com.samsung.framework.mapper.account;

import com.samsung.framework.domain.account.Account;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    List<AccountVO> getAccountList(SearchVO search);
    int findAllTotalCount(SearchVO search);
    int memberListCount();
    int memberListSearchCount(SearchVO search);
    List<AccountVO> getMemberList(SearchVO searchObject);
    AccountVO findMemberById(int id);
    int insertMember(AccountVO account);
    AccountVO findMemberByUserName(String userName);
    AccountVO getLoginInfo(String userId);
    int updatePwd(Account member);
    boolean existsByEmpNo(int empNo);

}
