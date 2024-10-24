package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.contract.documented.ContractCompletedService;
import com.samsung.framework.service.contract.documented.ContractSignRecallService;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/sign/completed")
@Controller
public class ContractCompletedController {
    private final ContractCompletedService contractCompletedService;

    private static final String BASE_URL = "https://demo.sign.netlock.hu";

    @Autowired
    private ContractCompletionController contractCompletionController;

    private boolean isSingle = true;

    public void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

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
     * [검색옵션] Contract Status
     * @return the list
     */
    @ModelAttribute("contractSearchStateTypeSelect")
    public List<SearchVO> contractStatusSelect() {
        return new SearchVO().getContractStatus();
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
        model.addAttribute("totalCount", contractCompletedService.getContractSignCompletedCount(null));
        return "contract/completed/list";
    }

/*
    @PostMapping(value = "/list")
    public String getContractSignCompletedList (Model model , @RequestBody SearchVO searchVO){

        log.info("completed 진입 !! ");
        // total
        int totalCount = contractCompletedService.getContractSignCompletedCount(searchVO);
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
        List<ContractVO> list = contractCompletedService.getContractSignCompletedList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/completed/list :: #content";
    }
*/

    @PostMapping(value = "/list")
    public String getContractSignCompletedList (Model model , @RequestBody SearchVO searchVO , HttpServletRequest request){

        String status ="";
        String referer = request.getHeader("Referer");

        if(referer.contains("status=")) {
            String[] params = referer.split("\\?")[1].split("&");
            // 각 파라미터를 순회하여 status 값을 찾음
            for (String param : params) {
                if (param.startsWith("status=")) {
                    status = param.split("=")[1];  // status 값을 추출하여 반환
                }
            }
            log.info("status >> " + status);
        }
        if(status.equals("success")){
            /* API 호출인데 로그인 인터셉트에서 걸림
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:3030/contract/create/downloadPdf";
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            */
            String returnString;
            if (isSingle) {
                returnString = contractCompletionController.downloadPdf();
            } else {
                returnString = contractCompletionController.multipleSigned();
            }
            log.info("returnString >> " + returnString);
        }

        // total
        int totalCount = contractCompletedService.getContractSignCompletedCount(searchVO);
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
        List<ContractVO> list = contractCompletedService.getContractSignCompletedList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/completed/list :: #content";
    }



    @GetMapping(value="/info/{seq}")
    public String getContractSignRecallInfo(Model model ,@PathVariable String seq){
        model.addAttribute("info",contractCompletedService.getContractSignCompletedInfo(seq));
        return "contract/completed/view";
    }


}
