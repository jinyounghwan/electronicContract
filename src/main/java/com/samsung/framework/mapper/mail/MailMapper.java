package com.samsung.framework.mapper.mail;

import com.samsung.framework.vo.member.MemberVO;

public interface MailMapper {
    void insertMailAuth(MemberVO member);
    MemberVO getAuthEmailMember(MemberVO member);

    MemberVO getAuthNumber(MemberVO member);
}
