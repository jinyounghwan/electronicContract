package com.samsung.framework.service.mail;


import com.samsung.framework.vo.account.AccountVO;

import java.util.Map;

public interface MailService {
    // 이메일 인증 메일을 보냄
    void mailAuthSend(AccountVO member);
    AccountVO getEmail(AccountVO member);
    Map<String,Object> getAuthNumber(AccountVO member) throws Exception;
}
