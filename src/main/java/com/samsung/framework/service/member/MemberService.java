package com.samsung.framework.service.member;

import com.samsung.framework.domain.common.SearchObject;
import com.samsung.framework.domain.common.TokenObjectVO;
import com.samsung.framework.domain.member.LoginRequest;
import com.samsung.framework.domain.member.Member;
import com.samsung.framework.domain.user.SignUpRequest;
import com.samsung.framework.vo.common.CollectionPagingVO;
import com.samsung.framework.vo.member.MemberVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.user.UserVO;

import java.util.List;
import java.util.Map;

public interface MemberService {

    // 멤버 목록 조회
    CollectionPagingVO getMemberList(SearchObject searchObject);

    // 멤버 상세 조회
    MemberVO findMemberById(int id);

    // 멤버 상세 조회
    MemberVO findMemberByUserName(String userName);

    // 멤버 등록
    Member insertMember(Member member);

    // 사용자 등록(w/ token)
    TokenObjectVO signUp(SignUpRequest signUpRequest);

    // 로그인 정보 조회
    MemberVO getLoginInfo(LoginRequest loginRequest);

    Map<String,Object> updatePwd(MemberVO member);

    // 전체 사용자 조회
    List<UserVO> findAllUsers(SearchVO searchVO);
}
