package com.samsung.framework.service.menu;

import com.samsung.framework.common.enums.RequestTypeEnum;
import com.samsung.framework.common.enums.TableNameEnum;
import com.samsung.framework.domain.account.SignUpRequest;
import com.samsung.framework.domain.menu.Menu;
import com.samsung.framework.mapper.authority.AuthorityMapper;
import com.samsung.framework.mapper.menu.MenuMapper;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.authority.MenuAuthorityVO;
import com.samsung.framework.vo.menu.MenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuService{
    private final MenuMapper menuMapper;
    private final AuthorityMapper authorityMapper;
    // 모든 임직원이 접근 가능한 메뉴
    private final List<String> EMPLOYEE_ACCESS_MENU_LIST = new ArrayList<>() {
                                                        {
                                                            add("MENU100000");
                                                            add("MENU104000");
                                                            add("MENU105000");
                                                        }
                                                    };

    /**
     * 메뉴 삽입
     * @param menu
     * @return 삽입 성공 1, 삽입 실패 0
     */
    public int insertMenu(Menu menu, AccountVO member){
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

    public Map<String, Object> saveAuthMenu(SignUpRequest signUpRequest){
        Map<String, Object> resultMap = new HashMap<>();
        List<MenuVO> menuList = new ArrayList<>(new ArrayList<>(getLgMenuList()).stream().toList());

        // 1depth 메뉴 가져오기
        List<MenuVO> menuMidList = getMidMenuList().stream().toList();

        menuList.addAll(menuMidList);

        // 2depth 메뉴 가져오기
        List<MenuVO> menuSmList = getSmMenuList();
        menuList.addAll(menuSmList);
        // menu_seq기준으로 정렬
        Collections.sort(menuList);

        List<MenuAuthorityVO> menuAuthorityList = new ArrayList<>();
        menuList.forEach(menu-> {
            MenuAuthorityVO menuAuthorityVO = null;
            if(EMPLOYEE_ACCESS_MENU_LIST.contains(menu.getMenuCode())){
                menuAuthorityVO = MenuAuthorityVO.builder()
                        .menuSeq(menu.getMenuSeq())
                        .authC("Y")
                        .authR("Y")
                        .authU("Y")
                        .authD("Y")
                        .authF("Y")
                        .grantTo(String.valueOf(signUpRequest.getEmpNo()))
                        .displayYn("Y")
                        .build();
            } else{
                menuAuthorityVO = MenuAuthorityVO.builder()
                        .menuSeq(menu.getMenuSeq())
                        .authC("N")
                        .authR("N")
                        .authU("N")
                        .authD("N")
                        .authF("N")
                        .grantTo(String.valueOf(signUpRequest.getEmpNo()))
                        .displayYn("Y")
                        .build();
            }
            menuAuthorityList.add(menuAuthorityVO);
        });
        int bulkInserted = authorityMapper.saveMenuAuthList(menuAuthorityList);
        if(bulkInserted < 0){
            throw new IllegalArgumentException("권한 메뉴 저장에 실패하였습니다.");
        }

        log.info("bulk Inserted : {} ", bulkInserted);
        return resultMap;
    }
}
