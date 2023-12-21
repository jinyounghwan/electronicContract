package com.samsung.framework.mapper.authority;

import com.samsung.framework.domain.authority.Authority;
import com.samsung.framework.mapper.common.GeneralMapper;
import com.samsung.framework.vo.authority.MenuAuthorityVO;
import com.samsung.framework.vo.code.CommonCodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorityMapper extends GeneralMapper {

    /**
     * 메뉴 조회
     * @param memberId
     * @param menuId
     * @return
     */
    MenuAuthorityVO selectMenuAuthority(@Param("memberId") String memberId, @Param("menuId") String menuId);

    /**
     * 부서 목록 조회
     * @return
     */
    List<CommonCodeVO> findDepartmentList();

    /**
     * 세부 메뉴이름으로 조회
     */
    List<CommonCodeVO> findMenuAuthority(List<String> menuNm);

    /**
     * 유저 Seq로 권한 조회
     * @param memberSeq
     * @return
     */
    List<Authority> findUserMenuAuthority(int memberSeq);
    /**
     * 개인 권한 저장
     * @param authority
     * @return
     */
    int saveIndividualAuthority(Authority authority);

    /**
     * 개인 권한 수정
     * @param authority
     * @return
     */
    int updateIndividualAuthority(Authority authority);

    /**
     * 엑셀 권한 수정
     */
    int updateExcelAuthority(Authority authority);
}
