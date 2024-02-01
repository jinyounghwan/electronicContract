package com.samsung.framework.controller.menu;

import com.samsung.framework.service.menu.MenuService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.menu.MenuVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
public class MenuController{
    private final MenuService menuService;
    /**
     * 대메뉴 리스트
     * @return 대메뉴 리스트
     */
    @PostMapping("/lgList")
    public List<MenuVO> getMenuList(HttpServletRequest request){
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        return menuService.getLgMenuList(account);
    }

    /**
     * 대메뉴 리스트
     * @return
     */
    @PostMapping("/midList")
    public List<MenuVO> getMenuMidList(HttpServletRequest request){
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        return menuService.getMidMenuList(account);
    }

    @PostMapping("/smList")
    public List<MenuVO> getMenuSmList(HttpServletRequest request){
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        return menuService.getSmMenuList(account);
    }

}
