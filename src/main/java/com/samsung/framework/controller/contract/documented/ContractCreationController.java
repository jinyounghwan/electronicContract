package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.common.utils.VariableHandlingUtil;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.CreateViewContract;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.contract.documented.ContractCompService;
import com.samsung.framework.service.contract.documented.ContractCreationService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;

/**
 * 계약서 생성 관련 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/create")
@Controller
public class ContractCreationController {

    private final ContractCreationService contractCreationService;

    private final AccountMapper accountMapper;

    private final ContractTemplateMapper contractTemplateMapper;

    private final ContractCompService contractCompService;

    private final VariableHandlingUtil variableHandlingUtil;

    private static final String BASE_URL = "https://demo.sign.netlock.hu";
    private static final String RETURN_URL = "https://www.netlock.hu";
    private static final String FILE_PATH = "C://files//electronicContract//upload//Contract//PDF//2409/24090501cf6df55e864e55bfbe75e0a5f5bd41.pdf";
    private static final String USERNAME = "dudghksdl45@gmail.com";
    private static final String PASSWORD = "!Jkj14789";

    // 파일 경로
    private static final String FILE_PATH_1 = "C://files//electronicContract//upload//Contract//PDF//2409/240905c88245ebb5274bbeafe8d7d8b714bf18.pdf";
    private static final String FILE_PATH_2 = "C://files//electronicContract//upload//Contract//PDF//2409/240905da84bf784e9946d2b30acc76dc1787ec.pdf";
    private static final String FILE_PATH_3 = "C://files//electronicContract//upload//Contract//PDF//2409/240905db9d85ef0cc647689edc94e5e42399c9.pdf";

    @ModelAttribute("templateCodeList")
    public List<Template> templatesOptionList() {
        return contractCreationService.getTemplateCode();
    }
    @GetMapping(value = {"/",""})
    public String contractCreation(){
        return "contract/creation/contract-creation";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity saveContract(@Valid SaveContractRequest saveContractReq , HttpServletRequest request) throws Exception {
        ResultStatusVO resultStatusVO = contractCreationService.saveContract(saveContractReq , request);
        return ResponseEntity.ok(resultStatusVO);
    }

    /*추가*/
    @GetMapping(value = "/viewContract")
    @ResponseBody
    public ResponseEntity createViewContract(CreateViewContract param){
        log.info("crate param  in!!!!!!!!!!!! >> " + param);
        ContractView ContractView =contractCreationService.getCreateContractView(param);

        ContractTemplateVO template = contractTemplateMapper.getContractTemplateInfo(StringUtil.getString(param.getTemplateSeq()));
        log.info(">> template = " + template);

        UserVO user = accountMapper.getUserInfo(param.getEmployeeId());
        log.info(">> USER : " + user);

        if(user == null) {
            ResultStatusVO resultStatusVO = new ResultStatusVO(400, ResultCodeMsgEnum.INVALID_EMP_NO.getMsg());
            return ResponseEntity.ok(resultStatusVO);
        }

        // contract Date 는 계약날짜니까 그거 받아와서 넣도록...?
        Variables replacementTarget = Variables.builder().name(user.getName()).employeeNo(StringUtil.getString(user.getEmpNo()))
                .contractDateEn(DateUtil.getStrContractDateEn(StringUtil.getString(param.getDate())))
                .contractDateHu(StringUtil.getString(param.getDate()).replaceAll("-","."))
                .salaryEn(param.getSalaryEn())
                .salaryHu(param.getSalaryHu())
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
        String replacedTitleEn  = variableHandlingUtil.replaceVariables(template.getContractTitleEn() , replacementTarget);
        // hu title
        String replacedTitleHu = variableHandlingUtil.replaceVariables(template.getContractTitleHu() , replacementTarget);
        // en content
        String replacedContentEn = variableHandlingUtil.replaceVariables(template.getContentsEn(), replacementTarget);
        // hu content
        String replacedContentHu = variableHandlingUtil.replaceVariables(template.getContentsHu() , replacementTarget);
        // en contract info
        String replacedContractInfoEn = "";
        if(!StringUtil.isEmpty(template.getContractInfoEn())){
            replacedContractInfoEn = variableHandlingUtil.replaceVariables(template.getContractInfoEn() , replacementTarget);
        }
        // hu contract info
        String replacedContractInfoHu = variableHandlingUtil.replaceVariables(template.getContractInfoHu() , replacementTarget);
        // en signatureArea
        String replacedEmployeeInfoEn = variableHandlingUtil.replaceVariables(template.getEmployeeInfoEn(), replacementTarget);
        // hu signatureArea
        String replacedEmployeeInfoHu = variableHandlingUtil.replaceVariables(template.getEmployeeInfoHu(), replacementTarget);

        template.setContractTitleEn(replacedTitleEn);
        template.setContractTitleHu(replacedTitleHu);
        template.setContentsEn(replacedContentEn);
        template.setContentsHu(replacedContentHu);
        template.setContractInfoEn(replacedContractInfoEn);
        template.setContractInfoHu(replacedContractInfoHu);
        template.setEmployeeInfoEn(replacedEmployeeInfoEn);
        template.setEmployeeInfoHu(replacedEmployeeInfoHu);

        return ResponseEntity.ok(template);
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

    @GetMapping(value = "/employCheck")
    @ResponseBody
    public ResponseEntity employCheck(CreateViewContract param){
        String errmsg = "";

        int employChk = contractCreationService.getEmployCheck(param);

        if(employChk < 0){
            errmsg = "Employee Id None";
        }else{
            errmsg = "Ok";
        }

        log.info("i >> " + employChk);
        return ResponseEntity.ok(employChk);
    }

    /* ECS API 테스트 single init */
    @PostMapping("/sendSignatureRequest")
    public ResponseEntity<String> sendSignatureRequest(HttpServletResponse response) {

        log.info("ECS TEST !! ");

        String url = "http://demo.sign.netlock.hu/rest/signature/singleInitSignature";

        // 파일 경로를 지정
        File file = new File("C://files//electronicContract//upload//Contract//PDF//2409/24090501cf6df55e864e55bfbe75e0a5f5bd41.pdf");
        FileSystemResource fileResource = new FileSystemResource(file);

        // 파라미터 구성
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("mimeType", "application/pdf");
        params.add("userId", "1920");
        params.add("fileName", "24090501cf6df55e864e55bfbe75e0a5f5bd41.pdf");
        params.add("signatureType", "PADES");
        params.add("signatureTypeLevel", "BASELINE_LT");
        params.add("file", fileResource); // 파일을 Body에 추가

        // Basic Auth 설정
        String username = "ecs.sdihu@partner.samsung.com";
        String password = "Testsdihu24!";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Body에 파일 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        // HTTP Entity 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate 사용하여 POST 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url + "?mimeType=application/pdf&userId=" + params.get("userId") +
                        "&fileName=24090501cf6df55e864e55bfbe75e0a5f5bd41.pdf&signatureType=" + params.get("signatureType") +
                        "&signatureTypeLevel=" + params.get("signatureTypeLevel"),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // CORS 헤더 추가
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3030");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        log.info("response !! >> "  + response);
        // 응답 반환
        return responseEntity;
    }

    /* ECS api 테스트 mutiple */
    @PostMapping("/init-signature")
    public ResponseEntity<?> initSignature(HttpServletRequest request, @RequestBody List<ContractCompVO> list) { //
        log.info("++ initSignature()");
        log.info(">> request = " + request);
        log.info(">> list = " + list);
//
        List<ContractCompVO> compList = contractCompService.getContractFileList(list);
        log.info(">> compList = " + compList);

        try {
            // 파일 해시 생성
            List<Map<String, String>> fileData = new ArrayList<>();
            fileData.add(createFileHash(FILE_PATH_1, "document1.pdf"));
            fileData.add(createFileHash(FILE_PATH_2, "document2.pdf"));
            fileData.add(createFileHash(FILE_PATH_3, "document3.pdf"));

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
                String signatureSessionToken = extractSessionToken(response.getBody());

                // 서명 URL 생성
                String signingUrl = BASE_URL + "/signature/" + signatureSessionToken + "?returnUrl=";

                log.info("signingUrl >> " + signingUrl);

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

    private String extractSessionToken(String responseBody) {
        return "b5fb9b76307caeb1b3a302f17d974af5"; // 예시 토큰, 실제 응답에 따라 수정해야 함
    }

}
