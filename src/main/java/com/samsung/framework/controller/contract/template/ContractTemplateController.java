package com.samsung.framework.controller.contract.template;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.contract.template.ContractTemplateService;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
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
import java.util.Map;

/**
 * 계약서 템플릿 관련 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/template")
@Controller
public class ContractTemplateController {

    private final ContractTemplateService contractTemplateService;
    /**
     * Search keyword type option list .
     * [검색옵션] 키워드
     * @return the list
     */
    @ModelAttribute("keywordTypeSelect")
    public List<SearchVO> searchKeywordTypeOptionList() {
        return new SearchVO().getSearcTempletehKeywordTypeOptionList();
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
    public String getContractTemplate(Model model){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount", contractTemplateService.getContractTemplateListCount(null));
        return "contract/template/list";
    }

    @PostMapping(value = "/list")
    public String getContractTemplateList (Model model , @RequestBody SearchVO searchVO){

        // total
        int totalCount = contractTemplateService.getContractTemplateListCount(searchVO);
        model.addAttribute("totalCount", totalCount);
        // paging
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(totalCount)
                .build();
        searchVO.setPaging(pagingVo);
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractTemplateVO> list = contractTemplateService.getContractTemplateList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/template/list :: #content";
    }

    @GetMapping(value="/info/{seq}")
    public String getContractTemplateInfo(Model model ,@PathVariable String seq){
        model.addAttribute("info",contractTemplateService.getContractTemplateInfo(seq));
        return "contract/template/view";
    }
    @GetMapping(value="/edit/{seq}")
    public String getContractTemplateInfoEdit(Model model ,@PathVariable String seq){
        model.addAttribute("info",contractTemplateService.getContractTemplateInfo(seq));
        return "contract/template/edit";
    }

    @PostMapping(value="/save")
    @ResponseBody
    public ResponseEntity saveContractTemplateInfo(ContractTemplateVO contractTemplateVO , HttpServletRequest request){
        //parameter 이름변경
        int result = contractTemplateService.saveContractTemplateInfo(contractTemplateVO , request);
        return new ResponseEntity(result, HttpStatus.OK);

    }
    @PostMapping(value="/copy")
    @ResponseBody
    public ResponseEntity saveContractTemplateCopyInfo(@RequestBody ContractTemplateVO contractTemplateVO , HttpServletRequest request){
        Map<String,Object> result = contractTemplateService.saveContractTemplateCopyInfo(contractTemplateVO , request);
        return new ResponseEntity(result, HttpStatus.OK);

    }




}
