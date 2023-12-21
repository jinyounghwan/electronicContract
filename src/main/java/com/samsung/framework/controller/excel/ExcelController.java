package com.samsung.framework.controller.excel;

import com.samsung.framework.controller.common.ParentController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/excel")
public class ExcelController extends ParentController {
    /**
     * excel 업로드
     * @param tableData
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public ResponseEntity excelUpload(@RequestBody String[][] tableData) throws Exception {
        String fileNm = getCommonService().getExcelPublicServiceImpl().createExcelFile(tableData);

        return new ResponseEntity(fileNm, HttpStatus.OK);
    }

}
