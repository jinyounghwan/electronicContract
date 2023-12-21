package com.samsung.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sample")
public class SampleController {

    private final SampleService SampleService;

    public SampleController(SampleService SampleService) {
        this.SampleService = SampleService;
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
}
