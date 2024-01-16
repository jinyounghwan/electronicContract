package com.samsung.framework.controller.excel;

import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FilePublicServiceImpl;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/excel")
public class ExcelController {

    private final FilePublicServiceImpl fileService;
    private final ExcelPublicServiceImpl excelService;

    /** TODO: 테이블 데이터 업로드
     * excel 업로드
     * @param tableData
     * @return
     * @throws Exception
     */
   /* @PostMapping("/upload")
    public ResponseEntity uploadExcel(@RequestBody String[][] tableData) throws Exception {
        String fileNm = getCommonService().getExcelPublicServiceImpl().createExcelFile(tableData);

        return new ResponseEntity(fileNm, HttpStatus.OK);
    }*/
    /**
     * excel 파일 읽기
     */
    @PostMapping("/write")
    public ResponseEntity readExcel(@RequestBody List<MultipartFile> multipartFiles) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<FilePublicVO> fileList = fileService.uploadFile(multipartFiles, "Contract/Excel");
        if(fileList.isEmpty()) {
            result.put("code","204");
            result.put("message", "엑셀 파일 저장 실패");
            result.put("errMsg", "incomplete");
        } else{
            result.put("code","200");
            result.put("message", "엑셀 파일 저장 성공");
        }
        excelService.readExcelFile(fileList);


        return ResponseEntity.ok(result);
    }
}
