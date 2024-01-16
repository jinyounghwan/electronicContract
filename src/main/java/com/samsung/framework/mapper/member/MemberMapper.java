package com.samsung.framework.mapper.member;

import com.samsung.framework.domain.member.Member;
import com.samsung.framework.vo.member.MemberVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.user.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    int memberListCount();
    int memberListSearchCount(SearchVO search);
    List<MemberVO> getMemberList(SearchVO searchObject);
    MemberVO findMemberById(int id);
    int insertMember(Member member);
    MemberVO findMemberByUserName(String userName);
    MemberVO getLoginInfo(String userName);
    int updatePwd(Member member);
    boolean existsByEmpNo(Long empNo);
    /**
     * 전체 사용자 조회
     * @return
     */
    List<UserVO> findAllUsers(SearchVO searchVO);

    /**
     * 저장
     * @param obj
     * @return
     */
    int insert(Object obj);
}
