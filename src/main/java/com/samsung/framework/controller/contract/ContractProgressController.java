package com.samsung.framework.controller.contract;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.service.contract.ContractProgressService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/progress")
@Controller
public class ContractProgressController {

    private final ContractProgressService contractProgressService;
    /**
     * Search date range option list list.
     * [검색옵션] 날짜
     * @return the list
     */
    @ModelAttribute("dateRangeSelect")
    public List<SearchVO> searchDateRangeOptionList() {
        return new SearchVO().getSearchDateRangeOptionList();
    }

    /**
     * Search keyword type option list list.
     * [검색옵션] 키워드
     * @return the list
     */
    @ModelAttribute("keywordTypeSelect")
    public List<SearchVO> searchKeywordTypeOptionList() {
        return new SearchVO().getSearchKeywordTypeOptionList();
    }
    /**
     * Search keyword type option  list.
     * [Doc.status] 키워드
     * @return the list
     */
    @ModelAttribute("contractDocSearchStateTypeSelect")
    public List<SearchVO>  searchContractDocSearchStateTypeSelect() {return new SearchVO().getContractDocSearchStateTypeList();}
    /**
     * Search keyword type option list list.
     * [contract status] 키워드
     * @return the list
     */
    @ModelAttribute("contractSearchStateTypeSelect")
    public List<SearchVO>  searchContractSearchStateTypeList() {return new SearchVO().getContractDocSearchStateTypeList();}

    @GetMapping(value={"","/"})
    public String getContractProgress(Model model){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount" ,contractProgressService.getContractProgressTotal(null));
        return "contract/contractProgress-list";
    }

    @PostMapping(value = "/list")
    public String getContractProgressList (Model model , @RequestBody SearchVO searchVO){
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(searchVO.getPaging().getTotalCount())
                .build();
        searchVO.setPaging(pagingVo);
        log.info("1==================================> :{}" ,searchVO.toString());
        // total
        int totalCount = contractProgressService.getContractProgressTotal(searchVO);
        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractVO> list = contractProgressService.getContractProgressList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        log.info("==================================> :{}" ,searchVO.toString());
        return "contract/contractProgress-list :: #content";
    }

    @PostMapping(value="/recall")
    @ResponseBody
    public ResponseEntity updateContractRecall(@RequestBody List<ProgressRequest> list){
        log.info("==============================> :{}" , list.toString());
        ResultStatusVO resultStatusVO = contractProgressService.updateContractRecall(list);
        return  ResponseEntity.ok(resultStatusVO);
    }

}
