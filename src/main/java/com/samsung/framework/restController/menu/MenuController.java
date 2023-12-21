/*
package com.samsung.framework.restController.menu;

import com.enums.common.framework.samsung.MapKeyStringEnum;
import com.enums.common.framework.samsung.RequestTypeEnum;
import com.enums.common.framework.samsung.ResultCodeMsgEnum;
import com.utils.common.framework.samsung.ObjectHandlingUtil;
import com.samsung.framework.restController.common.ParentController;
import com.common.domain.framework.samsung.DataObject;
import com.common.domain.framework.samsung.SearchObject;
import com.common.vo.framework.samsung.ResultStatusVO;
import com.menu.vo.framework.samsung.MenuVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class MenuController extends ParentController {

    */
/**
     * 대메뉴 조회
     * @return
     *//*

    @PostMapping("/getLgMenuList")
    public ResponseEntity getLgMenuList(){
        List<MenuVO> menuList = getCommonService().getMenuService().getLgMenuList();
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setListResultStatusVO(menuList, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.MENU_LIST.getKeyString());
        Map<String,Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, menuList);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 소메뉴 조회
     * @param searchObject
     * @return
     *//*

    @PostMapping("/getSmMenuList")
    public ResponseEntity getSmMenuList(@RequestBody @Valid SearchObject searchObject){
        List<MenuVO> menuList = getCommonService().getMenuService().getSmMenuList(searchObject.getSearch().getMenuSearch());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setListResultStatusVO(menuList, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.MENU_LIST.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, menuList);
        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 메뉴 단 건 조회
     * @param searchObject
     * @return
     *//*

    @GetMapping("/row")
    public ResponseEntity rowMenu(@RequestBody @Valid SearchObject searchObject){
        MenuVO menu = getCommonService().getMenuService().rowMenu(searchObject.getSearch().getMenuSearch());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setSingleObjResultStatusVO(menu, ResultCodeMsgEnum.NO_DATA);
        List<String> mapKeyList = Arrays.asList(MapKeyStringEnum.MENU.getKeyString());
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO, mapKeyList, menu);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 메뉴 저장
     * @param dataObject
     * @return
     *//*

    @PostMapping("/insert")
    public ResponseEntity insertMenu(@RequestBody @Valid DataObject dataObject){
        int inserted = getCommonService().getMenuService().insertMenu(dataObject.getData().getMenu(), getLoginVO());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(inserted, RequestTypeEnum.CREATE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 메뉴 내용 수정
     * @param dataObject
     * @return
     *//*

    @PostMapping("/update")
    public ResponseEntity updateMenu(@RequestBody @Valid DataObject dataObject){
        int inserted = getCommonService().getMenuService().updateMenu(dataObject.getData().getMenu(), getLoginVO());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(inserted, RequestTypeEnum.UPDATE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }

    */
/**
     * 메뉴 순서 변경 => 같은 Depth끼리만 가능
     * @param dataObject
     * @return
     *//*

    @PostMapping("/updateOrd")
    public ResponseEntity updateOrd(@RequestBody @Valid DataObject dataObject){
        int inserted = getCommonService().getMenuService().updateOrd(dataObject.getData().getMenuList(), getLoginVO());
        ResultStatusVO resultStatusVO = ObjectHandlingUtil.setDataManipulationResultStatusVO(inserted, RequestTypeEnum.UPDATE);
        Map<String, Object> resultMap = getMapUtil().responseEntityBodyWrapper(resultStatusVO);

        return ResponseEntity.ok(resultMap);
    }
}
*/
