package com.samsung.framework.service.menu;

import com.samsung.framework.common.enums.RequestTypeEnum;
import com.samsung.framework.common.enums.TableNameEnum;
import com.samsung.framework.domain.menu.Menu;
import com.samsung.framework.service.common.ParentService;
import com.samsung.framework.vo.member.MemberVO;
import com.samsung.framework.vo.menu.MenuVO;
import com.samsung.framework.vo.search.menu.MenuSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuServiceImpl extends ParentService implements MenuService {
    /**
     * 메뉴 삽입
     * @param menu
     * @return 삽입 성공 1, 삽입 실패 0
     */
    public int insertMenu(Menu menu, MemberVO member){
        Menu target = Menu.builder()
//              .pMenuSeq(menu.getPMenuSeq())
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

        int iAffectedRows = getCommonMapper().getMenuMapper().insert(target);

        return iAffectedRows;
    }


    /**
     * 메뉴 최대 ORD +1 값 조회
     * @param pMenuSeq
     * @return 최대 순서
     */
    public Long getOrdMax(Long pMenuSeq) {
        return getCommonMapper().getMenuMapper().getOrdMax(pMenuSeq);
    }

    /**
     * 메뉴 리스트 조회
     */
    public List<MenuVO> getMenuList(){
        return getCommonMapper().getMenuMapper().getMenuList();
    }

    /**
     * 대메뉴 리스트 조회
     * @return 대메뉴 List
     */
    public List<MenuVO> getLgMenuList(){
        return getCommonMapper().getMenuMapper().getLgMenuList();
    }

    /**
     * 중간 메뉴 리스트 조회
     * @return 중간 메뉴 List
     */
    public List<MenuVO> getMidMenuList(){
        return getCommonMapper().getMenuMapper().getMidMenuList();
    }

    /**
     * 소메뉴 리스트 조회
     * @return 소메뉴 List
     */
    public List<MenuVO> getSmMenuList(){
        return getCommonMapper().getMenuMapper().getSmMenuList();
    }


}
