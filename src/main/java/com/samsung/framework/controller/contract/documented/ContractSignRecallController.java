package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.contract.documented.ContractSignRecallService;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
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
        return "contract/recall/list";
    }

    @PostMapping(value = "/list")
    public String getContractSignRecallList (Model model , @RequestBody SearchVO searchVO){

        // total
        int totalCount = contractSignRecallService.getContractSignRecallCount(searchVO);
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
        List<ContractVO> list = contractSignRecallService.getContractSignRecallList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/recall/list :: #content";
    }
    @GetMapping(value="/info/{seq}")
    public String getContractSignRecallInfo(Model model ,@PathVariable String seq){
        model.addAttribute("info",contractSignRecallService.getContractSignRecallInfo(seq));
        return "contract/recall/view";
    }


    @GetMapping("/view/{seq}")
    public ResponseEntity<String> viewPdf(@PathVariable String seq) throws IOException {

        log.info("recall pdf view !!!! ");

        FilePublicVO filePathSel = contractSignRecallService.getFileSeq(seq);

        //String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/2405101d0a065adbeb497ea2621fa49959b54e.pdf"; // Replace with your actual file path
        String filePath = filePathSel.getStoragePath(); // file_integration > File Pat

        log.info("filePath select >" + filePath);

        byte[] pdfBytes = Files.readAllBytes(Paths.get(filePath));
        String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        log.info("base !! > " + base64EncodedPdf);

        return ResponseEntity.ok(base64EncodedPdf);
    }


}
