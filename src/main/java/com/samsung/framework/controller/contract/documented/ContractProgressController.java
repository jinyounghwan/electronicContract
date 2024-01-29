package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.service.contract.documented.ContractProgressService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        return "contract/progress/list";
    }

    @PostMapping(value = "/list")
    public String getContractProgressList (Model model , @RequestBody SearchVO searchVO){
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(searchVO.getPaging().getTotalCount())
                .build();
        searchVO.setPaging(pagingVo);
        // total
        int totalCount = contractProgressService.getContractProgressTotal(searchVO);
        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractVO> list = contractProgressService.getContractProgressList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/progress/list :: #content";
    }

    @PostMapping(value="/recall")
    @ResponseBody
    public ResponseEntity updateContractRecall(HttpServletRequest request ,@RequestBody List<ProgressRequest> list){
        ResultStatusVO resultStatusVO = contractProgressService.updateContractRecall(list ,request);
        return new ResponseEntity(resultStatusVO ,HttpStatus.OK);
    }
    @PostMapping(value="/assign")
    @ResponseBody
    public ResponseEntity updateContractAssign(HttpServletRequest request ,@RequestBody List<ProgressRequest> list){
        ResultStatusVO resultStatusVO = contractProgressService.updateContractAssign(list , request);
        return  new ResponseEntity(resultStatusVO ,HttpStatus.OK);
    }
    @GetMapping(value="/info/{seq}")
    public String getContractProgressInfo(Model model ,@PathVariable String seq){
        model.addAttribute("info",contractProgressService.getContractProgressInfo(seq));
        return "contract/progress/view";
    }
    @PostMapping(value="/recallInfo")
    @ResponseBody
    public ResponseEntity updateRecallInfo(HttpServletRequest request ,@RequestBody ProgressRequest progressRequest){
        ResultStatusVO result = contractProgressService.updateRecallInfo(progressRequest ,request);
        return new ResponseEntity(result, HttpStatus.OK);

    }
    @PostMapping(value="/assignInfo")
    @ResponseBody
    public ResponseEntity updateAssignInfo(HttpServletRequest request, @RequestBody ProgressRequest progressRequest){
        ResultStatusVO result = contractProgressService.updateAssignInfo(progressRequest ,request);
        return new ResponseEntity(result, HttpStatus.OK);

    }

}
