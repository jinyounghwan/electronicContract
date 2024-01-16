package com.samsung.framework.controller.record;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.record.RecordServiceImpl;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.record.RecordVO;
import com.samsung.framework.vo.search.record.RecordSearchVO;
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
public class RecordController {

    private final RecordServiceImpl recordService;

    /**
     * [검색옵션] 날짜
     */
    @ModelAttribute("searchDateRangeOptionList")
    public List<SelectOptionVO> searchDateRangeOptionList() {
        return recordService.getSearchDateRangeOptionList();
    }

    /**
     * [검색옵션] 키워드
     */
    @ModelAttribute("searchKeywordTypeOptionList")
    public List<SelectOptionVO> searchKeywordTypeOptionList() {
        return recordService.getSearchKeywordTypeList();
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
        int totalCount = recordService.totalCount(recordSearchVO);
        mv.addObject("totalCount", totalCount);

        // 목록 조회
        List<RecordVO> list = recordService.findAll(recordSearchVO);
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
        int totalListCount = recordService.totalCount(recordSearchVO);
        model.addAttribute("totalCount", totalListCount);

        // 목록 조회
        List<RecordVO> list = recordService.findAll(recordSearchVO);
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
        RecordVO rowData = recordService.findById(logSeq);
        model.addAttribute("rowData", rowData);

        return "record/list :: #detail_wrapper";
    }
}
