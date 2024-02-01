package com.samsung.framework.service.menu;

import com.samsung.framework.domain.account.SignUpRequest;
import com.samsung.framework.mapper.authority.AuthorityMapper;
import com.samsung.framework.mapper.menu.MenuMapper;
import com.samsung.framework.vo.authority.MenuAuthorityVO;
import com.samsung.framework.vo.menu.MenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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
