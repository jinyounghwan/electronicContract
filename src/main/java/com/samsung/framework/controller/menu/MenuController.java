package com.samsung.framework.controller.menu;

import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.vo.menu.MenuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController extends ParentController {
    @PostMapping("/list")
    public List<MenuVO> menuList(){
        return getCommonService().getMenuServiceImpl().getMenuList();
    }
}
