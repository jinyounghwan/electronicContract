package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.service.contract.documented.ContractProgressService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.ResourceLoader;

import java.io.*;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/progress")
@Controller
public class ContractProgressController{

    private final ContractProgressService contractProgressService;

    private final String PDF_STORAGE_PATH= File.separator+ "Contract" + File.separator + "PDF" +File.separator;

    @Value("${properties.file.rootDir}")
    private String getRootDir;

    @Value("${properties.file.realDir}")
    private String getRealDir;

    /**
     * Search date range option list list.
     * [검색옵션] 날짜
     * @return the list
     */
    @ModelAttribute("dateRangeSelect")
    public List<SearchVO> searchDateRangeOptionList() {
        return new SearchVO().getSearchDateRangeOptionList();
    }

    /**
     * Search keyword type option list list.
     * [검색옵션] 키워드
     * @return the list
     */
    @ModelAttribute("keywordTypeSelect")
    public List<SearchVO> searchKeywordTypeOptionList() {
        return new SearchVO().getSearchKeywordTypeOptionList();
    }
    /**
     * Search keyword type option  list.
     * [Doc.status] 키워드
     * @return the list
     */
    @ModelAttribute("contractDocSearchStateTypeSelect")
    public List<SearchVO>  searchContractDocSearchStateTypeSelect() {
//        list.add(new SearchVO( "PRCS1001","CREATED"));
//        list.add(new SearchVO("PRCS1002","ASSIGNED"));
        return new SearchVO().getContractSearchStateTypeList().stream()
                .filter(state -> state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN)) ||
                        state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.VIEWED)) ||
                        state.getCode().equals("ALL")
                )
                .collect(Collectors.toList());
    }
    /**
     * Search keyword type option list list.
     * [contract status] 키워드
     * @return the list
     */
    @ModelAttribute("contractSearchStateTypeSelect")
    public List<SearchVO>  searchContractSearchStateTypeList() {
//        list.add(new SearchVO("PRCS2001","UNSEEN"));
//        list.add(new SearchVO( "PRCS2002","VIEWED"));
        return new SearchVO().getContractDocSearchStateTypeList().stream()
                .filter(state -> state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.CREATED)) ||
                        state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.ASSIGNED)) ||
                        state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.RECALLED)) ||
                        state.getCode().equals("ALL")
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value={"","/"})
    public String getContractProgress(Model model){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount" ,contractProgressService.getContractProgressTotal(null));
        return "contract/progress/list";
    }

    @PostMapping(value = "/list")
    public String getContractProgressList (Model model , @RequestBody SearchVO searchVO){
        log.info("searchVO >> " + searchVO);
        // total
        int totalCount = contractProgressService.getContractProgressTotal(searchVO);
        model.addAttribute("totalCount", totalCount);
        // paging
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(totalCount)
                .build();
        searchVO.setPaging(pagingVo);
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractVO> list = contractProgressService.getContractProgressList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);

        return "contract/progress/list :: #content";
    }

    @PostMapping(value="/recall")
    @ResponseBody
    public ResponseEntity updateContractRecall(HttpServletRequest request ,@RequestBody List<ProgressRequest> list){
        ResultStatusVO resultStatusVO = contractProgressService.updateContractRecall(list ,request);
        return new ResponseEntity(resultStatusVO ,HttpStatus.OK);
    }
    @PostMapping(value="/assign")
    @ResponseBody
    public ResponseEntity updateContractAssign(HttpServletRequest request ,@RequestBody List<ProgressRequest> list){
        ResultStatusVO resultStatusVO = contractProgressService.updateContractAssign(list , request);
        return  new ResponseEntity(resultStatusVO ,HttpStatus.OK);
    }
    @GetMapping(value="/info/{seq}")
    public String getContractProgressInfo(Model model ,@PathVariable String seq) throws IOException {
        log.info("/info/{seq} in!! ");
        model.addAttribute("info",contractProgressService.getContractProgressInfo(seq));
        log.info("/info/{seq} in!! >> " + contractProgressService.getContractProgressInfo(seq));
        return "contract/progress/view";
    }
    @PostMapping(value="/recallInfo")
    @ResponseBody
    public ResponseEntity updateRecallInfo(HttpServletRequest request ,@RequestBody ProgressRequest progressRequest){
        ResultStatusVO result = contractProgressService.updateRecallInfo(progressRequest ,request);
        return new ResponseEntity(result, HttpStatus.OK);

    }
    @PostMapping(value="/assignInfo")
    @ResponseBody
    public ResponseEntity updateAssignInfo(HttpServletRequest request, @RequestBody ProgressRequest progressRequest){
        ResultStatusVO result = contractProgressService.updateAssignInfo(progressRequest ,request);
        return new ResponseEntity(result, HttpStatus.OK);

    }


    /* pdf view Test */
    /*
    @GetMapping("/viewPdf")
    public ModelAndView previewPdf(HttpServletRequest request , HttpServletResponse response) throws IOException{
        // pdf 파일 가져오기
        String filePath = request.getParameter("filePath"); //파라미터로 파일경로 전달
        ServletContext context = request.getServletContext();
        InputStream is = context.getResourceAsStream(filePath);

        // pdf 파일 스트림 처리
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int readBytes;
        while ((readBytes = is.read(buffer)) != -1){
            baos.write(buffer , 0 , readBytes);
        }

        // http 응답 구성
        Resource resource = new ByteArrayResource(baos.toByteArray());
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION  , "inline; filename=" + filePath);
        headers.add(HttpHeaders.CONTENT_TYPE , "application/pdf");
        headers.setCacheControl("no-cache , no-store , must-revalidate");
        headers.setPragma("no-cache");

        // view 반환 설정
        ModelAndView modelAndView = new ModelAndView("view"); // view 화면 이름
        modelAndView.addObject("filePath" , filePath);
        modelAndView.addObject("pdfData" , resource);

        return modelAndView;
    }
    */
    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getPDF(@PathVariable String fileName) {

        // PDF file path generation
        String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/" + fileName;
        //String filePath = "src/main/resources/static/img/" + fileName;

        log.info("pdf pre view in!222!!" + filePath);

        // Read PDF file
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] fileData = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(fileData.length)
                    .body(fileData);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/view/{seq}")
    public ResponseEntity<String> viewPdf(@PathVariable String seq) throws IOException {

        FilePublicVO filePathSel = contractProgressService.getFileSeq(seq);

        //String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/2405101d0a065adbeb497ea2621fa49959b54e.pdf"; // Replace with your actual file path
        String filePath = filePathSel.getStoragePath(); // file_integration > File Pat

        log.info("filePath select >" + filePath);

        byte[] pdfBytes = Files.readAllBytes(Paths.get(filePath));
        String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        log.info("base !! > " + base64EncodedPdf);

        return ResponseEntity.ok(base64EncodedPdf);
    }
    @GetMapping("/getPdf")
    public void getPdf(HttpServletResponse response) throws IOException {
        log.info("get PDF Call API !!!");
        String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/2405101d0a065adbeb497ea2621fa49959b54e.pdf";
        //String filePath = "src/main/resources/static/img/2405109f9d3a50422c4a73b9ca10959ce2787f.pdf";
        File file = new File(filePath);

        log.info("file >> " + file);
        if (file.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=2405101d0a065adbeb497ea2621fa49959b54e.pdf");

            FileInputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
        }
    }

    /* 안씀 */
    @GetMapping("/getLocation")
    public ResponseEntity<String> getLocation() throws Exception {
        //현재 날짜
        String nowDay = DateUtil.getUtcNowDateFormat("yyMM");

        //final String storagePath = FileUtil.getOsRootDir() + getRootDir + getRealDir + PDF_STORAGE_PATH + nowDay + "/240520cc53a8d2bf1d4c8cb9ebb57f2df3ef6c.pdf";
        final String storagePath =FileUtil.getOsRootDir() + getRootDir + getRealDir +  "/240520cc53a8d2bf1d4c8cb9ebb57f2df3ef6c.pdf";
        return ResponseEntity.ok(storagePath);
    }

    @GetMapping("/viewTest")
    public ResponseEntity<byte[]> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // PDF 파일 경로
        //String filePath = "C:\\upload\\abc.pdf";
        String filePath = "C:\\files\\electronicContract\\upload\\Contract\\PDF\\2405\\2405204e390fce77a341a2ae1d4f23dfdfa431.pdf";

        // PDF 파일 읽기
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();

        // 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(bytes.length);

        // ResponseEntity 객체 생성하여 PDF 데이터 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}