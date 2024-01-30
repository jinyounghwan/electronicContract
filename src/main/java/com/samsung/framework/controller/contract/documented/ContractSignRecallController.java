package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.contract.documented.ContractSignRecallService;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/sign/recall")
@Controller
public class ContractSignRecallController {
    private final ContractSignRecallService contractSignRecallService;

    /**
     * Search keyword type option list .
     * [검색옵션] 키워드
     * @return the list
     */
    @ModelAttribute("keywordTypeSelect")
    public List<SearchVO> searchKeywordTypeOptionList() {
        return new SearchVO().getSearchKeywordTypeOptionList();
    }

    /**
     * Search date range option list .
     * [검색옵션] 날짜
     * @return the list
     */
    @ModelAttribute("dateRangeSelect")
    public List<SearchVO> searchDateRangeOptionList() {
        return new SearchVO().getSearchDateRangeOptionList();
    }
    @GetMapping(value = {"/",""})
    public String getContractSignRecall(Model model){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount", contractSignRecallService.getContractSignRecallCount(null));
        return "contract/Recall/list";
    }

    @PostMapping(value = "/list")
    public String getContractSignRecallList (Model model , @RequestBody SearchVO searchVO){
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(searchVO.getPaging().getTotalCount())
                .build();
        searchVO.setPaging(pagingVo);
        // total
        int totalCount = contractSignRecallService.getContractSignRecallCount(searchVO);
        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractVO> list = contractSignRecallService.getContractSignRecallList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/Recall/list :: #content";
    }
    @GetMapping(value="/info/{seq}")
    public String getContractSignRecallInfo(Model model ,@PathVariable String seq){
        model.addAttribute("info",contractSignRecallService.getContractSignRecallInfo(seq));
        return "contract/Recall/view";
    }
}
