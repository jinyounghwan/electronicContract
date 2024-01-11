package com.samsung.framework.controller.record;

import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.record.RecordVO;
import com.samsung.framework.vo.search.record.RecordSearchVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController extends ParentController {

    /**
     * [검색옵션] 날짜
     */
    @ModelAttribute("searchDateRangeOptionList")
    public List<SelectOptionVO> searchDateRangeOptionList() {
        return getCommonService().getRecordService().getSearchDateRangeOptionList();
    }

    /**
     * [검색옵션] 키워드
     */
    @ModelAttribute("searchKeywordTypeOptionList")
    public List<SelectOptionVO> searchKeywordTypeOptionList() {
        return getCommonService().getRecordService().getSearchKeywordTypeList();
    }

    /**
     * 이력 목록 조회
     * @param mv
     * @return
     */
    @GetMapping({"", "/"})
    public ModelAndView findAll(ModelAndView mv) {
        mv.setViewName("record/list");
        mv.addObject("rowData", new RecordVO());

        var recordSearchVO = new RecordSearchVO();
        recordSearchVO.setPaging(Paging.builder()
                .currentPage(1)
                .displayRow(10)
                .build()
        );

        // 전체 게시물 수
        int totalCount = getCommonService().getRecordService().totalCount(recordSearchVO);
        mv.addObject("totalCount", totalCount);

        // 목록 조회
        List<RecordVO> list = getCommonService().getRecordService().findAll(recordSearchVO);
        mv.addObject("list", list);

        // paging
        Paging paging = ObjectHandlingUtil.pagingOperatorBySearch(recordSearchVO, totalCount);
        mv.addObject("paging", paging);

        return mv;
    }

    /** API **/
    /**
     * 이력 목록 조회 API
     * @param model
     * @param paramMap
     * @return
     */
    @PostMapping("/api/list")
    public String findAll(Model model, @RequestBody Map<String, Object> paramMap) {
        ObjectMapper om = new ObjectMapper();
        var recordSearchVO = om.convertValue(paramMap.get("recordSearchVO"), RecordSearchVO.class);
        Paging pagingParam = om.convertValue(paramMap.get("paging"), Paging.class);
        recordSearchVO.setPaging(Paging.builder()
                .currentPage(pagingParam.getCurrentPage())
                .displayRow(pagingParam.getDisplayRow())
                .totalCount(pagingParam.getTotalCount())
                .build()
        );

        // 전체 게시물 수
        int totalListCount = getCommonService().getRecordService().totalCount(recordSearchVO);
        model.addAttribute("totalCount", totalListCount);

        // 목록 조회
        List<RecordVO> list = getCommonService().getRecordService().findAll(recordSearchVO);
        model.addAttribute("list", list);

        // paging
        Paging paging = Paging.builder()
                .currentPage(recordSearchVO.getPaging().getCurrentPage())
                .displayRow(recordSearchVO.getPaging().getDisplayRow())
                .totalCount(totalListCount)
                .build();
        model.addAttribute("paging", paging);

        return "record/list :: #list_wrapper";
    }

    /**
     * 이력 상세 API
     * @param model
     * @param logSeq
     * @return
     */
    @GetMapping("/api/{logSeq}")
    public String findAll(Model model, @PathVariable long logSeq) {
        // 이력 상세 조회
        RecordVO rowData = getCommonService().getRecordService().findById(logSeq);
        model.addAttribute("rowData", rowData);

        return "record/list :: #detail_wrapper";
    }
}
