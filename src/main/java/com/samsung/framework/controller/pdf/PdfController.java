package com.samsung.framework.controller.pdf;


import com.samsung.framework.controller.common.ParentController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfController extends ParentController {
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody String html) throws Exception {
        getCommonService().getPdfService().createPDF(html);

        return new ResponseEntity(HttpStatus.OK);
    }
}
