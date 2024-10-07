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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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
    public ResponseEntity download(@RequestPart(value="param") Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.info("pdf 다운 !!! " + param.get("seq"));
        String html = StringUtil.getString(param.get("html"));
        String seq = StringUtil.getString(param.get("seq"));
        log.info("pdf 다운 !!! html >> " + html);
        FilePublicVO file = pdfService.createPDF(html, request , seq);

        return fileService.downloadFile(file,request, response);
    }

    @PostMapping("/create/download")
    public ResponseEntity createDownload(@RequestPart(value="param") Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String html = StringUtil.getString(param.get("html"));
        String seq = "";
        FilePublicVO file = pdfService.createPDF(html, request , seq);

        fileService.downloadFile(file,request, response);
        String fileName = file.getName();

        return ResponseEntity.ok(fileName);
    }
}
