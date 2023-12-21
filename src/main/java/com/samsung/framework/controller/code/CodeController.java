package com.samsung.framework.controller.code;


import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.vo.code.CodePublicVO;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.code.CodeSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeController extends ParentController {

    /**
     * [검색옵션] 코드 그룹 선택
     */
    @ModelAttribute("commonCodeGroupList")
    public List<SelectOptionVO> commonCodeGroupList() {
        return getCommonService().getCodeService().commonCodeGroupList();
    }

    /**
     * [검색옵션] 구분 선택
     * @return
     */
    @ModelAttribute("commonCodeCategoryList")
    public List<SelectOptionVO> commonCodeCategoryList() {
        return getCommonService().getCodeService().commonCodeCategoryList("MENU");
    }

    /**
     * 공통코드 목록 조회
     * @param mv
     * @return
     */
    @GetMapping({"", "/"})
    public ModelAndView findAll(ModelAndView mv) {
        mv.setViewName("code/list");
        CodeSearchVO codeSearchVO = new CodeSearchVO();
        codeSearchVO.setCode("MENU");
        List<CommonCodeVO> list = getCommonService().getCodeService().findAll(codeSearchVO);
        mv.addObject("list", list);

        return mv;
    }


/** API **/
    @PostMapping("/list")
    @ResponseBody
    public List<CodePublicVO> codeList(){
        return getCommonService().getCodeService().listCode();
    }

    /**
     * 공통 코드 수정 API
     * @param tgt
     * @return
     */
    @PutMapping("/api")
    public String update(Model model, @RequestBody List<CommonCodeVO> tgt) {
        Map<String, Object> result = getCommonService().getCodeService().updateCommonCode(tgt);
        List<CommonCodeVO> list = new ArrayList<>();
        if((int)result.get("code") == 200) {
            list = getCommonService().getCodeService().findAll(new CodeSearchVO());
        }
        model.addAttribute("list", list);

        return "code/list :: #list_wrapper";
    }

    @GetMapping("/api")
    public String findAll(Model model, @RequestParam("codeCd") String codeCd) {
        CodeSearchVO codeSearchVO = new CodeSearchVO();
        codeSearchVO.setCode(codeCd.substring(0, 4));
        List<CommonCodeVO> list = getCommonService().getCodeService().findAll(codeSearchVO);
        model.addAttribute("list", list);

        return "code/list :: #list_wrapper";
    }
}
