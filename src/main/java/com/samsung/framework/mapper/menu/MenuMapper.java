package com.samsung.framework.mapper.menu;

import com.samsung.framework.domain.authority.Authority;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.menu.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    // Menu Ord 최대 순서 조회
    Long getOrdMax(Long pMenuSeq);

    // 메뉴 리스트
    List<MenuVO> getMenuList();
    // 대메뉴 리스트 조회
    List<MenuVO> getLgMenuList();

    // 2depth Menu 리스트 조회
    List<MenuVO> getMidMenuList();

    // 소메뉴 리스트 조회
    List<MenuVO> getSmMenuList();

    // [공통 코드] 메뉴 1 depth 조회
    List<MenuVO> findCommonMenuCodeList();

    // [공통 코드] 메뉴 조회
    List<CommonCodeVO> findMenuList(String deptCd);

    // 메뉴에 대한 권한 여부 조회
    long findMenuSeqByUserId(Authority authority);

    /**
     * 저장
     * @param obj
     * @return
     */
    int insert(Object obj);
}
