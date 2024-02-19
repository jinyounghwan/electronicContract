package com.samsung.framework.controller.pdf;


import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.service.pdf.PdfService;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfController {
    @Autowired
    PdfService pdfService;
    @Autowired
    FileService fileService;
    @PostMapping("/download")
    public ResponseEntity download(@RequestBody Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String html = StringUtil.getString(param.get("html"));
        FilePublicVO file = pdfService.createPDF(html);

        return fileService.downloadFile(file, request, response);
    }
}
