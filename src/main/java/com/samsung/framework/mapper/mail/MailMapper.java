package com.samsung.framework.mapper.mail;


import com.samsung.framework.vo.account.AccountVO;

public interface MailMapper {
    void insertMailAuth(AccountVO member);
    AccountVO getAuthEmailMember(AccountVO member);

    AccountVO getAuthNumber(AccountVO member);
}
