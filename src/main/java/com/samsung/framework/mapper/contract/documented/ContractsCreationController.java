package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.common.enums.FileTypeEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.common.utils.VariableHandlingUtil;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.service.contract.documented.ContractsCreationService;
import com.samsung.framework.service.pdf.PdfService;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    private final AccountMapper accountMapper;
    private final VariableHandlingUtil variableHandlingUtil;
    private final ContractCreationMapper contractCreationMapper;

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
        List<UserVO> userList = new ArrayList<>();
        List<String> contractList = new ArrayList<>();
        List<String> salaryHuList = new ArrayList<>();
        List<String> salaryEnList = new ArrayList<>();

        int successCnt = 0;
        int errorCnt = 0;
        List<String> errorEmpNoList = new ArrayList<>();


        //엑셀 데이터 읽기
        for (MultipartFile file : multipartFiles) {
            // Process each uploaded file
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet

            // Start reading data from 3rd row (index 2)
            for (int i = 2; i < sheet.getLastRowNum() + 1; i++) {
                int id =0;

                Row row = sheet.getRow(i);

                // Extract data from each row
                String date = row.getCell(0).getStringCellValue();

                if(row.getCell(1).getCellType() == CellType.NUMERIC){
                    id = (int) row.getCell(1).getNumericCellValue();
                }else{
                    log.info("문자형식 데이터가 있습니다.");
                }

                String employeeNo = row.getCell(2).getStringCellValue();
                String salaryHu = row.getCell(3).getStringCellValue();
                String salaryEn = row.getCell(4).getStringCellValue();

                UserVO user = accountMapper.getUserInfo(employeeNo);
                // errorEmpNoList 에 먼저 추가한 후에 아래에서 검증되면 제거한다.
                errorEmpNoList.add(employeeNo);

                // id 값이 0이면 tempSeq 에 없는 값
                // user 가 null 이면 excelList2 에 add 하지 않는다.
                if (id != 0 && user != null) {
                    excelList2.add(id);
                    userList.add(user);
                    contractList.add(date);
                    salaryHuList.add(salaryHu);
                    salaryEnList.add(salaryEn);
                    // 검증된 employeeNo 제거
                    errorEmpNoList.remove(employeeNo);
                } else {
                    errorCnt++;
                }
            }
        }


        excelList =  contractsCreationService.getExcelSelect(excelList2);

        for(int i=0;i<excelList.size();i++) {
            // user 가 null 이면 NullException 에러발생
            if (userList.get(i) != null) {
                successCnt++;

                UserVO user = userList.get(i);
                String contractDate = contractList.get(i);
                String salaryHu = salaryHuList.get(i);
                String salaryEn = salaryEnList.get(i);

                Variables replacementTarget = Variables.builder().name(user.getName()).employeeNo(StringUtil.getString(user.getEmpNo()))
                        .contractDateEn(DateUtil.getStrContractDateEn(StringUtil.getString(contractDate)))
                        .contractDateHu(StringUtil.getString(contractDate).replaceAll("-", "."))
                        .salaryEn(salaryEn)
                        .salaryHu(salaryHu)
                        .hireDateEn(user.getHireDateEn())
                        .hireDateHu(user.getHireDateHu())
                        .jobTitleEn(user.getJobTitle())
                        .jobTitleHu(user.getJobTitle())
                        .salaryNo(user.getSalaryNo())
                        .wageTypeEn(user.getWageType())
                        .wageTypeHu(replaceWageType(user.getWageType()))
                        .build();
                log.info(">> replacementTarget = " + replacementTarget);
                // en title
                String replacedTitleEn = variableHandlingUtil.replaceVariables(excelList.get(i).getContractTitleEn(), replacementTarget);
                // hu title
                String replacedTitleHu = variableHandlingUtil.replaceVariables(excelList.get(i).getContractTitleHu(), replacementTarget);
                // en content
                String replacedContentEn = variableHandlingUtil.replaceVariables(excelList.get(i).getContentsEn(), replacementTarget);
                // hu content
                String replacedContentHu = variableHandlingUtil.replaceVariables(excelList.get(i).getContentsHu(), replacementTarget);
                // en contract info
                String replacedContractInfoEn = "";
                if (!StringUtil.isEmpty(excelList.get(i).getContractInfoEn())) {
                    replacedContractInfoEn = variableHandlingUtil.replaceVariables(excelList.get(i).getContractInfoEn(), replacementTarget);
                }
                // hu contract info
                String replacedContractInfoHu = variableHandlingUtil.replaceVariables(excelList.get(i).getContractInfoHu(), replacementTarget);
                // en signatureArea
                String replacedEmployeeInfoEn = variableHandlingUtil.replaceVariables(excelList.get(i).getEmployeeInfoEn(), replacementTarget);
                // hu signatureArea
                String replacedEmployeeInfoHu = variableHandlingUtil.replaceVariables(excelList.get(i).getEmployeeInfoHu(), replacementTarget);

                excelList.get(i).setContractTitleEn(replacedTitleEn);
                excelList.get(i).setContractTitleHu(replacedTitleHu);
                excelList.get(i).setContentsEn(replacedContentEn);
                excelList.get(i).setContentsHu(replacedContentHu);
                excelList.get(i).setContractInfoEn(replacedContractInfoEn);
                excelList.get(i).setContractInfoHu(replacedContractInfoHu);
                excelList.get(i).setEmployeeInfoEn(replacedEmployeeInfoEn);
                excelList.get(i).setEmployeeInfoHu(replacedEmployeeInfoHu);
            }
        }

        log.info("successCnt = " + successCnt);
        log.info("errorCnt = " + errorCnt);


        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data" , excelList);
        resultMap.put("successCnt" , successCnt);
        resultMap.put("errorCnt" , errorCnt);
        resultMap.put("errorEmpNoList", errorEmpNoList);

        return ResponseEntity.ok().body(resultMap);
    }


    @ResponseBody
    @PostMapping("/bulk-upload")
    public ResponseEntity bulkUpload(@RequestPart(value="file", required = true) List<MultipartFile> multipartFiles, HttpServletRequest request) throws Exception {
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

        log.info("FilePublicVO = " + file);

        String fileName = file.getName();

        return ResponseEntity.ok(fileName);
    }

    private String replaceWageType(String type){
        String replaceType ="";
        switch (type) {
            case "M" -> {
                replaceType =  "hó";
            }
            case "H" -> {
                replaceType =  "óra";
            }
            default -> {
                replaceType = "";
            }
        }
        return replaceType;
    }

}
