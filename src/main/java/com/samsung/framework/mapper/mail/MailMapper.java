package com.samsung.framework.mapper.mail;


import com.samsung.framework.vo.account.AccountVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {
    void insertMailAuth(AccountVO member);
    AccountVO getAuthEmailMember(AccountVO member);

    AccountVO getAuthNumber(AccountVO member);
}
