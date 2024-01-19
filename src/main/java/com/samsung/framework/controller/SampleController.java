package com.samsung.framework.controller;

import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.vo.search.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sample")
@Slf4j
public class SampleController {

    private final SampleService sampleService;
    private final FileUtil fileUtil;

    public SampleController(SampleService SampleService , FileUtil fileUtil, FileUtil fileUtil1) {
        this.sampleService = SampleService;
        this.fileUtil = fileUtil1;
    }

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

    @GetMapping("/add")
    public String addView(Model model) {
        model.addAttribute("sample", new SampleVO("0", "default", "default", null, "Y"));
        return "sample/add-sample";
    }

    @PostMapping("/addRedirect")
    public RedirectView addRedirect(@ModelAttribute("sample") SampleVO sampleVO, RedirectAttributes redirectAttributes) {
        final RedirectView redirectView = new RedirectView("", true);
        SampleVO savedSampleVO = sampleService.addSample(sampleVO);
        redirectAttributes.addFlashAttribute("savedBook", savedSampleVO);
        redirectAttributes.addFlashAttribute("addBookSuccess", true);
        return redirectView;
    }

    @GetMapping(value = "/example")
    public String sampleView(){
        return "sample/example";
    }

    @PostMapping(value = "/saveSignatureImg")
    @ResponseBody
    public ResponseEntity saveSignatureImg(@RequestParam(value = "signatureImg")List<MultipartFile> files){
        log.info("param  111111::::::::! " +  files);
        //Map<String,Object> fileMap = SampleService.saveSignatur(files);
        return ResponseEntity.ok(null);
    }

    @PostMapping(value = "/saveSignature")
    @ResponseBody
    public ResponseEntity saveSignature(@RequestBody  Map<String,Object> contractValue){
        log.info("param 22222222::::::::::::::::::: " +  contractValue.toString());
        return ResponseEntity.ok(null);

    }

    @GetMapping(value = "/sample")
    public String getSample (){
        return "sample/sample";
    }
    @PostMapping(value = "/getSampleList")
    public String getSampleList (Model model , @RequestBody SearchVO searchVO){
        // total
        int totalCount = sampleService.getSampleListTotal(searchVO);
        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",new Paging());
//        model.addAttribute("paging111111", Paging.builder()
//                .currentPage(searchVO.getPaging().getCurrentPage())
//                .displayRow(searchVO.getPaging().getDisplayRow())
//                .totalCount(totalCount)
//                .build());

        // list
//        List<Map<String,Object>> list = sampleService.getSampleList(searchVO);
//        model.addAttribute("list",list);
        log.info("model =======================> :{} ",model.toString());
        return "sample/sample :: #content";
//        return ResponseEntity.ok(null);
    }


    @GetMapping(value="/sample2")
    public String getSample2() {
        return "sample/sample";
    }
}
