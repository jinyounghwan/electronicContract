package com.samsung.framework.controller.excel;

import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.domain.file.File;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class ExcelController extends ParentController {
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
        List<FilePublicVO> fileList = getCommonService().getFileServiceImpl().uploadFile(multipartFiles, "CONTRACT");
        List<FilePublicVO> saveFileList = getCommonService().getFileServiceImpl().saveFile(fileList);
        if(saveFileList.isEmpty()) {
            result.put("code","204");
            result.put("message", "파일 저장에 실패하였습니다.");
            result.put("errMsg", "incomplete");
        } else{
            result.put("code","200");
            result.put("message", "파일 저장에 성공하였습니다.");
        }
        getCommonService().getExcelPublicServiceImpl().readExcelFile(multipartFiles);
        return ResponseEntity.ok(result);
    }
}
