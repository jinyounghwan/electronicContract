package com.samsung.framework.controller.pdf;


import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.service.file.FilePublicServiceImpl;
import com.samsung.framework.service.pdf.PdfService;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfController extends ParentController {
    @Autowired
    PdfService pdfService;
    @Autowired
    FilePublicServiceImpl fileService;
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String html = StringUtil.getString(param.get("html"));
        FilePublicVO file = pdfService.createPDF(html);
        List<FilePublicVO> fileList = new ArrayList<>();
        fileList.add(file);

        return new ResponseEntity(HttpStatus.OK);
    }
}
