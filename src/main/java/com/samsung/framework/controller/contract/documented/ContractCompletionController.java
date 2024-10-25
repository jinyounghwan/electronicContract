package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.domain.contract.paper.ContractComp;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.account.ghr.GhrAccountService;
import com.samsung.framework.service.contract.documented.ContractCompService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 계약서 완료 Controller
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/contract/sign/comp")
public class ContractCompletionController {
    private final ContractCompService contractCompletionService;
    private final GhrAccountService ghrAccountService;
    private final ContractCompService contractCompService;

    private static final String BASE_URL = "https://demo.sign.netlock.hu";
    private static final String RETURN_URL = "https://www.netlock.hu";
    private static final String FILE_PATH = "C://files//electronicContract//upload//Contract//PDF//2409/24090501cf6df55e864e55bfbe75e0a5f5bd41.pdf";
    private static final String USERNAME = "dudghksdl45@gmail.com";
    private static final String PASSWORD = "!Jkj14789";
    @Autowired
    @Lazy
    private ContractCompletedController contractCompletedController;

    // Api 세션 토큰
    String signatureSessionToken ="";

    // ContractNo
    Long contractNoQes ;

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
        return new SearchVO().getContractDocSearchStateTypeList().stream()
                .filter(state -> state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.COMPLETED)) ||
                        state.getCode().equals(ContractProcessEnum.processCode(ContractProcessEnum.PAPER_CONTRACT)) ||
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
        return new SearchVO().getContractDocSearchStateTypeList();
    }

    @ModelAttribute("templateTypeSelect")
    public List<Template> templateTypeOptionList(){
        return contractCompletionService.getTemplateCode();
    }

    @GetMapping({"","/"})
    public String getCompletionContractList(Model model){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount" ,contractCompletionService.getContractCompTotal(new SearchVO()));
        return "contract/completion/completionList";
    }

    @PostMapping("/list")
    public String getContractCompList(Model model, @RequestBody SearchVO searchVO){
        log.info("완료 계약서 vo >> " + searchVO);
        // total
        int totalCount = contractCompletionService.getContractCompTotal(searchVO);

        Paging pagingVO =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(totalCount)
                .build();
        searchVO.setPaging(pagingVO);

        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging", pagingVO);

        // list
        List<ContractCompVO> list = contractCompletionService.getContractCompList(searchVO);
        model.addAttribute("list",list);
        model.addAttribute("search", searchVO);

        return "contract/completion/completionList :: #content-wrapper";
    }

    @GetMapping("/paper")
    public ModelAndView getPaperContractUpload(ModelAndView mv) {
        mv.setViewName("contract/completion/paperCompletionUpload");
        return mv;
    }

    @ResponseBody
    @PostMapping("/api/paper/registration")
    public ResponseEntity createPaperContract(HttpServletRequest request, @RequestPart(value="data", required = true) ContractCompVO contract , @RequestPart(value="file", required = true)List<MultipartFile> file) throws Exception {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        ResultStatusVO resultStatusVO = contractCompletionService.paperContractSave(request, contract, account, file);

        return ResponseEntity.ok(resultStatusVO);
//        return new ResponseEntity<>(result, returnStatus);
    }

    @GetMapping("/detail/{contractNo}")
    public ModelAndView getContractCompDetail(ModelAndView mv, @PathVariable long contractNo){
        ContractCompVO contract = contractCompletionService.getContractCompDetail(contractNo);
        contractNoQes = contract.getContractNo();
        log.info("contractNoQes >>  " + contractNoQes);
        mv.setViewName("contract/completion/contractCompletion-detail");
        mv.addObject("info", contract);

        return mv;
    }

    @GetMapping("/view/{seq}")
    public ResponseEntity<String> viewPdf(@PathVariable String seq) throws IOException {

        FilePublicVO filePathSel = contractCompletionService.getFileSeq(seq);

        //String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/2405101d0a065adbeb497ea2621fa49959b54e.pdf"; // Replace with your actual file path
        String filePath = filePathSel.getStoragePath(); // file_integration > File Pat

        log.info("filePath select >" + filePath);

        byte[] pdfBytes = Files.readAllBytes(Paths.get(filePath));
        String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        log.info("base !! > " + base64EncodedPdf);

        return ResponseEntity.ok(base64EncodedPdf);
    }

    /* ECS API 테스트 single init */
    @PostMapping("/sendSignatureRequest")
    public ResponseEntity<String> sendSignatureRequest(HttpServletResponse response) {

        log.info("ECS TEST !! ");
        String seq = contractNoQes.toString();
        FilePublicVO filePathSel = contractCompletionService.getFileSeq(seq);


        Path path = Paths.get(filePathSel.getStoragePath());
        log.info("path >> "+ path);

        // 파일 경로를 지정
        // 이부분 나중에 바꿔줘야함 지금은 있는 파일로 테스트
        // 준택책임
        // File file = new File("/Users/juntaek/Documents/pdf/upload/Contract/PDF/2410/24102264ad49bbab054756b764a67414184d01.pdf");

        // 영환책임
        File file = new File(String.valueOf(path));
        FileSystemResource fileResource = new FileSystemResource(file);

        log.info("filePathSel.getName() >> " + filePathSel.getName());

        // 파라미터 구성
        /*
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("mimeType", "application/pdf");
            params.add("userId", "1920");
            params.add("fileName", filePathSel.getName());
            params.add("signatureType", "PADES");
            params.add("signatureTypeLevel", "BASELINE_LT");
            params.add("file", fileResource); // 파일을 Body에 추가
        */

        // Basic Auth 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(USERNAME, PASSWORD);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Body에 파일 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        // HTTP Entity 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // URL에 파라미터 설정
        String url = BASE_URL + "/rest/signature/singleInitSignature?mimeType=application/pdf"
                + "&userId=1920"
                + "&signatureType=PADES"
                + "&signatureTypeLevel=BASELINE_LT"
                + "&fileName="+filePathSel.getName();


        // RestTemplate 사용하여 POST 요청
        RestTemplate restTemplate = new RestTemplate();

        // POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // CORS 헤더 추가
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3030");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        log.info("response !! >> "  + response);
        log.info("responseEntity !! >> "  + responseEntity);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // responseBody에서 signatureSessionToken 추출
            signatureSessionToken = extractSessionToken(responseEntity.getBody());

            // 서명 URL 생성
            String signingUrl = BASE_URL + "/signature/" + signatureSessionToken + "?returnUrl=http://localhost:3030/contract/sign/completed";
            contractCompletedController.setIsSingle(true);
            log.info("signingUrl >> " + signingUrl);

            // 결과 반환
            return ResponseEntity.ok(signingUrl);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }

    }

    private String multiSignatureSessionToken;
    private List<ContractCompVO> toUpdateList;

    /* ECS api 테스트 mutiple */
    @PostMapping("/init-signature")
    public ResponseEntity<?> initSignature(HttpServletRequest request, @RequestBody List<ContractCompVO> list) {

        List<ContractCompVO> compList = contractCompService.getContractFileList(list);
        toUpdateList = compList;

        try {
            // 파일 해시 생성
            List<Map<String, String>> fileData = new ArrayList<>();

            for (ContractCompVO item : compList) {
                fileData.add(createFileHash(item.getStoragePath(), item.getFileName()));
            }

            // REST API 요청을 위한 RestTemplate 설정
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(USERNAME, PASSWORD));

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 바디 생성
            HttpEntity<List<Map<String, String>>> requestEntity = new HttpEntity<>(fileData, headers);

            // URL에 파라미터 설정
            String url = BASE_URL + "/rest/signature/multipleInitSignature?mimeType=application/pdf"
                    + "&userId=1920"
                    + "&signatureType=PADES"
                    + "&signatureTypeLevel=BASELINE_LT"
                    + "&fileHashType=SHA-256";

            // POST 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // responseBody에서 signatureSessionToken 추출
                multiSignatureSessionToken = extractSessionToken(response.getBody());

                // 서명 URL 생성
                String signingUrl = BASE_URL + "/signature/" + multiSignatureSessionToken + "?returnUrl=http://localhost:3030/contract/sign/completed";

                contractCompletedController.setIsSingle(false);
                // 결과 반환
                return ResponseEntity.ok(signingUrl);
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/multipleSigned")
    public String multipleSigned() {

        List<ContractCompVO> compList; // = contractCompService.getContractFileList(list);
        compList = toUpdateList;

        int successCnt = 0;
        int failCnt = 0;

        log.info(">> compList = " + compList.toString());

        for (ContractCompVO item : compList) {
            // multiple signed URL 만들기
            String url = BASE_URL + "/rest/signature/" + multiSignatureSessionToken + "/multipleSignedDocs?"
                    + "fileName=" + item.getFileName()
                    + "&mimeType=application/pdf"
                    + "&signatureType=PADES"
                    + "&signatureTypeLevel=BASELINE_LT";

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(USERNAME, PASSWORD);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            try {
                createFileHash(item.getStoragePath(), item.getFileName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // RequestEntity 사용하여 파일 전송
            HttpEntity<InputStreamResource> requestEntity = null;
            try {
                requestEntity = createRequestEntity(item);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            // POST 요청 보내기
            // REST API 요청을 위한 RestTemplate 설정
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                try {
                    // 랜덤 숫자 채번
                    Random random = new Random();
                    StringBuilder randomNumber = new StringBuilder();

                    for (int i = 0; i < 12; i++) {
                        int digit = random.nextInt(10);  // 0부터 9까지의 숫자를 생성
                        randomNumber.append(digit);
                    }
                    // 파일 저장 경로 설정
                    Path path = Paths.get("/Users/juntaek/Documents/pdf/upload/Contract/PDF/signed/" + randomNumber + ".pdf");

                    // 폴더가 존재하지 않으면 생성
                    File directory = new File(path.getParent().toString());
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    // PDF 파일 저장
                    Files.write(path, responseEntity.getBody());

                    ContractCompVO contractCompVO = new ContractCompVO();
                    contractCompVO.setContractNo(item.getContractNo());

                    // DB 업데이트 (QES  서명완료)
                    contractCompService.qesUpdateYn(contractCompVO);

                    successCnt++;
                } catch (IOException e) {
                    e.printStackTrace();
                    failCnt++;
                }
            } else {
                failCnt++;
            }
        }

        if (failCnt > 0) {
            return "PDF 파일 다운로드에 실패했습니다. 상태 코드: ";
        } else {
            return "PDF 파일이 성공적으로 저장 되었습니다: ";
        }
    }


    private HttpEntity<InputStreamResource> createRequestEntity(ContractCompVO item) throws FileNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(USERNAME, PASSWORD);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 바이너리 파일로 설정

        // 파일을 InputStreamResource로 래핑
        File file = new File(item.getStoragePath());
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));

        return new HttpEntity<>(inputStreamResource, headers);
    }

    private Map<String, String> createFileHash(String filePath, String fileName) throws Exception {
        // 파일을 읽고 SHA-256 해시 생성
        Path pdfPath = Paths.get(filePath);
        byte[] pdfBytes = Files.readAllBytes(pdfPath);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileHashBytes = digest.digest(pdfBytes);
        String fileHash = Base64.getEncoder().encodeToString(fileHashBytes);

        // 파일 이름과 해시 값을 포함한 Map 생성
        Map<String, String> fileData = new HashMap<>();
        fileData.put("fileName", fileName);
        fileData.put("fileHash", fileHash);

        return fileData;
    }


    /* ECS API 3번 테스트 */
    @GetMapping("/downloadPdf")
    @ResponseBody
    public String downloadPdf() {
        ContractCompVO contractCompVO = new ContractCompVO();
        // 랜덤 숫자 채번
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int digit = random.nextInt(10);  // 0부터 9까지의 숫자를 생성
            randomNumber.append(digit);
        }

        // 파라미터
        String url = "https://demo.sign.netlock.hu/rest/signature/"+signatureSessionToken+"/singleSignedDoc";
        String username = "dudghksdl45@gmail.com";
        String password = "!Jkj14789";

        // Authorization 헤더 생성
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);

        // GET 요청을 위한 RestTemplate 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // PDF 파일을 받을 수 있는 ByteArrayResource로 응답 처리
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {

                // 파일 저장 경로 설정 ->> 추후 바꿔야함 경로
                Path path = Paths.get("C:\\files\\electronicContract\\upload\\"+randomNumber+".pdf");

                // 폴더가 존재하지 않으면 생성
                File directory = new File(path.getParent().toString());
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // PDF 파일 저장
                Files.write(path, response.getBody());

                contractCompVO.setContractNo(contractNoQes);
                contractCompVO.setQesPdfPath(path.toString());

                // Contract_No쓰고 다시 초기화
                contractNoQes= 0L;

                // DB 업데이트 (QES  서명완료)
                contractCompService.qesUpdateYn(contractCompVO);

                return "PDF 파일이 성공적으로 다운로드되었습니다: ";
            } catch (IOException e) {
                e.printStackTrace();
                return "파일 저장 중 오류가 발생했습니다.";
            }
        } else {
            return "PDF 파일 다운로드에 실패했습니다. 상태 코드: ";
        }
    }


    private String extractSessionToken(String responseBody) {
        return responseBody; // 예시 토큰, 실제 응답에 따라 수정해야 함
    }


}
