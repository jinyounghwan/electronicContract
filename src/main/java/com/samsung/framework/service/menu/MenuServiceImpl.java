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


    /**
     * 메뉴 단 건 조회
     * @param searchVO
     * @return Menu Object
     */
    public MenuVO rowMenu(MenuSearchVO searchVO) {
        return (MenuVO) getCommonMapper().getMenuMapper().rowBySearch(searchVO);
    }

    /**
     * 메뉴 내용 수정
     * @param menu
     * @return 수정 성공 1, 수정 실패 0
     */
    public int updateMenu(Menu menu, MemberVO member) {

        // menuId에 해당하는 menuSeq 조회
        Long menuSeq = (Long) getCommonMapper().getMenuMapper().rowByKey(menu.getMenuCode());

         Menu target = Menu.builder()
                            .name(menu.getName())
                            .menuCode(menu.getMenuCode())
//                          .menuUrl(menu.getMenuUrl())
                            .displayYn(menu.getDisplayYn())
                            .tableName(TableNameEnum.MENU.name())
                            .logId1(String.valueOf(menuSeq))
                            .logType(RequestTypeEnum.UPDATE.getRequestType())
                            .logId2(null)
                            .logJson(null)
                            .remark(null)
                            .regId(member.getUserId())
                            .build();

         int iAffectedRows = getCommonMapper().getMenuMapper().update(target);
         
         return iAffectedRows;
    }

    /**
     * 메뉴 순서 변경
     * @param menuList
     * @return 변경 성공 1 , 변경 실패 0
     */
    public Integer updateOrd(List<Menu> menuList, MemberVO member){

        Iterator<Menu> iter = menuList.iterator();

        int iAffectedRows=0;
        while(iter.hasNext()) {
            Menu menu = iter.next();
            Long menuSeq = (Long) getCommonMapper().getMenuMapper().rowByKey(menu.getMenuCode());
            Menu target = Menu.builder()
                                .menuCode(menu.getMenuCode())
                                .ord(menu.getOrd())
//                              .pMenuSeq(menu.getPMenuSeq())
                                .tableName(TableNameEnum.MENU.name())
                                .logId1(String.valueOf(menuSeq))
                                .logType(RequestTypeEnum.UPDATE.getRequestType())
                                .logId2(null)
                                .logJson(null)
                                .remark(null)
                                .regId(member.getUserId())
                                .build();

            iAffectedRows = getCommonMapper().getMenuMapper().updateOrd(target);
            if(iAffectedRows<=0) {
                break;
            }
            
        }

        /* int iAffectedRows  = getCommonMapper().getMenuMapper().updateOrd(menuList); */

        return iAffectedRows;
    }
}
