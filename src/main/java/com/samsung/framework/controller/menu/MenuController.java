package com.samsung.framework.controller.menu;

import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.vo.menu.MenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController extends ParentController {

    /**
     * 대메뉴 리스트
     * @return 대메뉴 리스트
     */
    @PostMapping("/LgList")
    public List<MenuVO> menuList(){
        return getCommonService().getMenuServiceImpl().getLgMenuList();
    }

    /**
     * 대메뉴 리스트
     * @return
     */
    @PostMapping("/midList")
    public List<MenuVO> menuMidList(){
        return getCommonService().getMenuServiceImpl().getMidMenuList();
    }

    @PostMapping("/smList")
    public List<MenuVO> menuSmList(){
        return getCommonService().getMenuServiceImpl().getSmMenuList();
    }
}
