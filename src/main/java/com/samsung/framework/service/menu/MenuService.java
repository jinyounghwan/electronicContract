package com.samsung.framework.service.menu;

import com.samsung.framework.common.enums.RequestTypeEnum;
import com.samsung.framework.common.enums.TableNameEnum;
import com.samsung.framework.domain.menu.Menu;
import com.samsung.framework.mapper.menu.MenuMapper;
import com.samsung.framework.vo.member.MemberVO;
import com.samsung.framework.vo.menu.MenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuService{
    private final MenuMapper menuMapper;
    /**
     * 메뉴 삽입
     * @param menu
     * @return 삽입 성공 1, 삽입 실패 0
     */
    public int insertMenu(Menu menu, MemberVO member){
        Menu target = Menu.builder()
                .menuCode(menu.getMenuCode())
                .name(menu.getName())
                .ord(menu.getOrd())
                .displayYn(menu.getDisplayYn())
                .useYn(menu.getUseYn())
                .tableName(TableNameEnum.MENU.name())
                .logId1("")
                .logType(RequestTypeEnum.CREATE.getRequestType())
                .logId2(null)
                .logJson(null)
                .remark(null)
                .regId(member.getUserId())
                .build();

        int iAffectedRows = menuMapper.insert(target);

        return iAffectedRows;
    }


    /**
     * 메뉴 최대 ORD +1 값 조회
     * @param pMenuSeq
     * @return 최대 순서
     */
    public Long getOrdMax(Long pMenuSeq) {
        return menuMapper.getOrdMax(pMenuSeq);
    }

    /**
     * 메뉴 리스트 조회
     */
    public List<MenuVO> getMenuList(){
        return menuMapper.getMenuList();
    }

    /**
     * 대메뉴 리스트 조회
     * @return 대메뉴 List
     */
    public List<MenuVO> getLgMenuList(){
        return menuMapper.getLgMenuList();
    }

    /**
     * 중간 메뉴 리스트 조회
     * @return 중간 메뉴 List
     */
    public List<MenuVO> getMidMenuList(){
        return menuMapper.getMidMenuList();
    }

    /**
     * 소메뉴 리스트 조회
     * @return 소메뉴 List
     */
    public List<MenuVO> getSmMenuList(){
        return menuMapper.getSmMenuList();
    }


}
