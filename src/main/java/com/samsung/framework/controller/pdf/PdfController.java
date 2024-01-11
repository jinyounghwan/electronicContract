package com.samsung.framework.controller.pdf;


import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.controller.common.ParentController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfController extends ParentController {
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody Map<String,Object> param) throws Exception {
        String html = StringUtil.getString(param.get("html"));

        //이미지 태그 안닫힌 태그들 찾아서 닫는 작업 진행
        String aaa = "";
        Pattern pattern  =  Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        Matcher match = pattern.matcher(html);
        while(match.find()){
            String imgTag   = match.group();
            String imgTag2  = imgTag.replaceAll(">", "/>");
            html = html.replaceAll(imgTag, imgTag2);
        }
        log.info("aaaaa ::::::::::: : {}" ,html);
        String replace = html.replaceAll("\"", "'");
        log.info("replace :::::::::::::::::: :{}" , replace);
        getCommonService().getPdfService().createPDF(replace);

        return new ResponseEntity(HttpStatus.OK);
    }
}
