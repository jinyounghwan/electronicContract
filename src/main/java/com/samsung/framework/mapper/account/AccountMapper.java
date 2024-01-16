package com.samsung.framework.mapper.account;

import com.samsung.framework.domain.account.Account;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    int findAllTotalCount(SearchVO searchVO);
    int memberListCount();
    int memberListSearchCount(SearchVO search);
    List<AccountVO> getMemberList(SearchVO searchObject);
    AccountVO findMemberById(int id);
    int insertMember(AccountVO account);
    AccountVO findMemberByUserName(String userName);
    AccountVO getLoginInfo(String userId);
    int updatePwd(Account member);
    boolean existsByEmpNo(int empNo);
    /**
     * 전체 사용자 조회
     * @return
     */
    List<AccountVO> findAllUsers(SearchVO searchVO);
}
