package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.common.enums.FileTypeEnum;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.service.contract.documented.ContractsCreationService;
import com.samsung.framework.service.pdf.PdfService;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contracts/create")
@Controller
public class ContractsCreationController {

    private final ContractsCreationService contractsCreationService;
    private final PdfService pdfService;
    private final FileService fileService;

    @GetMapping(value={"/",""})
    public String contractBulkCreation(Model model){
        model.addAttribute("fileSeq", FileTypeEnum.EXCEL.getTemplateDownloadFileSeq());
        return "contract/creation/bulk-contract-creation";
    }

    /*
    *
    @ResponseBody
    @PostMapping("/bulk-upload")
    public ResponseEntity bulkUpload(@RequestPart(value="file", required = true) List<MultipartFile> multipartFiles , HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap= contractsCreationService.bulkUpload(multipartFiles , request);
        return ResponseEntity.ok().body(resultMap);
    *
    *
    * */


    @ResponseBody
    @PostMapping("/bulk-uploadTest")
    public ResponseEntity bulkUploadTest(@RequestPart(value="file", required = true) List<MultipartFile> multipartFiles , HttpServletRequest request) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<ContractTemplateVO> excelList= new ArrayList<>();
        List<Integer> excelList2= new ArrayList<>();
        int id =0;
        //엑셀 데이터 읽기
        for (MultipartFile file : multipartFiles) {
            // Process each uploaded file
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet

            // Start reading data from 3rd row (index 2)
            for (int i = 2; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);

                // Extract data from each row
                String date = row.getCell(0).getStringCellValue();

                if(row.getCell(1).getCellType() == CellType.NUMERIC){
                    id = (int) row.getCell(1).getNumericCellValue();
                }else{
                    log.info("문자형식 데이터가 있습니다.");
                }

                String text = row.getCell(2).getStringCellValue();
                double amount = row.getCell(3).getNumericCellValue();
                String employeeNo = row.getCell(4).getStringCellValue();
                String department = row.getCell(5).getStringCellValue();

                // Create a data map and add it to the list
                Map<String, Object> rowData = new HashMap<>();
                //rowData.put("date", date);
                rowData.put("templateDetailsSeq", id);
                //rowData.put("text", text);
                //rowData.put("amount", amount);
                //rowData.put("employeeNo", employeeNo);
                //rowData.put("department", department);

                // Print row data for debugging purposes
                log.info("Row DataTest: " + rowData);

                excelList2.add(id);

            }
        }

        excelList =  contractsCreationService.getExcelSelect(excelList2);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data" , excelList);

        return ResponseEntity.ok().body(resultMap);
    }


    @ResponseBody
    @PostMapping("/bulk-upload")
    public ResponseEntity bulkUpload(@RequestPart(value="file", required = true) List<MultipartFile> multipartFiles , HttpServletRequest request) throws Exception {
        log.info("bulk-upload>> in!! ");
        Map<String, Object> resultMap= contractsCreationService.bulkUpload(multipartFiles , request);
        return ResponseEntity.ok().body(resultMap);
    }

    public void ExcelData(List<MultipartFile> multipartFiles) throws IOException {
        log.info("excelData In !! ");
        for (MultipartFile file : multipartFiles) {
            // Process each uploaded file
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet

            // Start reading data from 3rd row (index 2)
            for (int i = 2; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);

                // Extract data from each row
                String date = row.getCell(0).getStringCellValue();
                int id = (int) row.getCell(1).getNumericCellValue();
                String text = row.getCell(2).getStringCellValue();
                double amount = row.getCell(3).getNumericCellValue();
                String employeeNo = row.getCell(4).getStringCellValue();
                String department = row.getCell(5).getStringCellValue();

                // Create a data map and add it to the list
                Map<String, Object> rowData = new HashMap<>();
                rowData.put("date", date);
                rowData.put("id", id);
                rowData.put("text", text);
                rowData.put("amount", amount);
                rowData.put("employeeNo", employeeNo);
                rowData.put("department", department);

                // Print row data for debugging purposes
                log.info("Row Data: " + rowData);
            }
        }
    }

    // String filePath = "D:/Dev2/temp/ContractBulkCreationUploadTemplate.xlsx"; // 절대 경로
    @GetMapping("/downloadExcel")
    public ResponseEntity<Resource> downloadExcel() throws IOException {
        // 1. 로컬 엑셀 파일 읽기
        File file = new File("D:/Dev2/temp/ContractBulkCreationUploadTemplate.xlsx");
        Resource resource = new UrlResource(file.toURI());

        log.info("file.getName() >> " + file.getName());
        // 2. HTTP 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));

        // 3. ResponseEntity 객체 생성하여 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }


    @PostMapping("/uploadExcelData")
    public void uploadExcelData(@RequestParam("excelFile") MultipartFile excelFile) throws IOException {

        log.info("uploadExcelData in!! ");
        // Apache POI 라이브러리 로딩
        Workbook workbook = WorkbookFactory.create(excelFile.getInputStream());

        // 첫 번째 시트 가져오기
        Sheet sheet = workbook.getSheetAt(0);

        // 날짜 형식 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 데이터 읽기 및 로그 출력
        for (int i = 2; i < sheet.getLastRowNum() + 1; i++) {
            log.info("for in!! ");
            Row row = sheet.getRow(i);


            String templateCode = row.getCell(1).getStringCellValue();
            String templateType = row.getCell(2).getStringCellValue();
            String empNo = row.getCell(3).getStringCellValue();
            double salaryHu = row.getCell(4).getNumericCellValue();
            double salaryEn = row.getCell(5).getNumericCellValue();

            log.info("코드번호: {}, 타입: {}, 사번: {}, 헝가리어 급여: {}, 영어 급여: {}",
                    templateCode, templateType, empNo, salaryHu, salaryEn);
        }
    }

    @PostMapping("/pdfDownload")
    public ResponseEntity createDownload(@RequestPart(value="param") Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("pdf Down In !!!! >> ");
        log.info("pdf/ create download param>> " + param);
        log.info("pdf/ create download request>> " + request);
        log.info("pdf/ create download response>> " + response);


        String html = StringUtil.getString(param.get("html"));

        log.info("pdf/ create download html>> " + html);
        FilePublicVO file = pdfService.createPDF(html, request);

        fileService.downloadFile(file,request, response);

        log.info("file Name Con >> " + file.getName());

        String fileName = file.getName();

        return ResponseEntity.ok(fileName);
    }

}
