package com.samsung.framework.controller;

import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

/**
 * The type Sample controller.
 */
@Controller
@RequestMapping("/sample")
@Slf4j
public class SampleController {

    private final SampleService sampleService;
    private final FileUtil fileUtil;

    /**
     * Instantiates a new Sample controller.
     *
     * @param SampleService the sample service
     * @param fileUtil      the file util
     * @param fileUtil1     the file util 1
     */
    public SampleController(SampleService SampleService , FileUtil fileUtil, FileUtil fileUtil1) {
        this.sampleService = SampleService;
        this.fileUtil = fileUtil1;
    }

    /**
     * View string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping({"", "/"})
    public String view(Model model) {
//        Collection<SampleVO> sampleList = SampleService.getSampleList();
        SampleVO sample = sampleService.getSample();

        model.addAttribute("sample", sample);

        var checkboxList = Arrays.asList("Basketball", "Baseball", "Golf");
        model.addAttribute("checkboxList", checkboxList);

        var radioList = Arrays.asList("Yes", "No", "N/A");
        model.addAttribute("radioList", radioList);

        Map radioMap = new HashMap();
        radioMap.put("Y", "받아요");
        radioMap.put("N", "안 받아요");
        radioMap.put("N/A", "해당 없어요");
        model.addAttribute("radioMap", radioMap);

        return "sample/index";
    }

    /**
     * Add view string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/add")
    public String addView(Model model) {
        model.addAttribute("sample", new SampleVO("0", "default", "default", null, "Y"));
        return "sample/add-sample";
    }

    /**
     * Add redirect redirect view.
     *
     * @param sampleVO           the sample vo
     * @param redirectAttributes the redirect attributes
     * @return the redirect view
     */
    @PostMapping("/addRedirect")
    public RedirectView addRedirect(@ModelAttribute("sample") SampleVO sampleVO, RedirectAttributes redirectAttributes) {
        final RedirectView redirectView = new RedirectView("", true);
        SampleVO savedSampleVO = sampleService.addSample(sampleVO);
        redirectAttributes.addFlashAttribute("savedBook", savedSampleVO);
        redirectAttributes.addFlashAttribute("addBookSuccess", true);
        return redirectView;
    }

    /**
     * Sample view string.
     * 페이지이동
     * @return the string
     */
    @GetMapping(value = "/example")
    public String sampleView(){
        return "sample/example";
    }

    /**
     * Save signature img response entity.
     * 서명 된 이미지 저장
     * @param files the files
     * @return the response entity
     */
    @PostMapping(value = "/saveSignatureImg")
    @ResponseBody
    public ResponseEntity saveSignatureImg(@RequestParam(value = "signatureImg")List<MultipartFile> files){
        log.info("param  111111::::::::! " +  files);
        //Map<String,Object> fileMap = SampleService.saveSignatur(files);
        return ResponseEntity.ok(null);
    }

    /**
     * Save signature response entity.
     * 계약서 저장 테스트
     * @param contractValue the contract value
     * @return the response entity
     */
    @PostMapping(value = "/saveSignature")
    @ResponseBody
    public ResponseEntity saveSignature(@RequestBody  Map<String,Object> contractValue){
        log.info("param 22222222::::::::::::::::::: " +  contractValue.toString());
        return ResponseEntity.ok(null);

    }

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
     * Get sample string.
     *   샘플페이지 이동
     *  처음 접근시 빈 객체 (페이징 , 리스트)를 model에 넣어준다
     *
     * @param model the model
     * @return the string
     */
    @GetMapping(value = "/sample")
    public String getSample (Model model){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount" ,sampleService.getSampleListTotal(null));
        return "sample/sample";
    }

    /**
     * Get sample list string.
     * 목록
     *
     * @param model    the model
     * @param searchVO the search vo
     * @return the string
     */
    @PostMapping(value = "/getSampleList")
    public String getSampleList (Model model , @RequestBody SearchVO searchVO){
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(searchVO.getPaging().getTotalCount())
                .build();
        searchVO.setPaging(pagingVo);
        // total
        int totalCount = sampleService.getSampleListTotal(searchVO);
        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",pagingVo);

        // list
        List<Map<String,Object>> list = sampleService.getSampleList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "sample/sample :: #wrapper";
    }

}
