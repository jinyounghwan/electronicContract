package com.samsung.framework.mapper.menu;

import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.menu.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    // 메뉴 리스트
    List<MenuVO> getMenuList();
    // 대메뉴 리스트 조회
    List<MenuVO> getLgMenuList(AccountVO account);

    // 2depth Menu 리스트 조회
    List<MenuVO> getMidMenuList(AccountVO account);

    // 소메뉴 리스트 조회
    List<MenuVO> getSmMenuList(AccountVO account);

}
