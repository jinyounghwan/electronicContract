package com.samsung.framework.controller;

import com.samsung.framework.common.utils.FileUtil;
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

    private final SampleService SampleService;
    private final FileUtil fileUtil;

    public SampleController(SampleService SampleService , FileUtil fileUtil, FileUtil fileUtil1) {
        this.SampleService = SampleService;
        this.fileUtil = fileUtil1;
    }

    @GetMapping({"", "/"})
    public String view(Model model) {
//        Collection<SampleVO> sampleList = SampleService.getSampleList();
        SampleVO sample = SampleService.getSample();

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
        SampleVO savedSampleVO = SampleService.addSample(sampleVO);
        redirectAttributes.addFlashAttribute("savedBook", savedSampleVO);
        redirectAttributes.addFlashAttribute("addBookSuccess", true);
        return redirectView;
    }

    @GetMapping(value = "/example")
    public String sampleView(){
        return "sample/example";
    }

    @PostMapping(value = "/saveSignatur")
    @ResponseBody
    public ResponseEntity saveSignatur(@RequestParam(value = "signatureImg")List<MultipartFile> files){
        log.info("param  ::::::::! " +  files);
        Map<String,Object> fileMap = SampleService.saveSignatur(files);
        return ResponseEntity.ok(null);
    }
}
