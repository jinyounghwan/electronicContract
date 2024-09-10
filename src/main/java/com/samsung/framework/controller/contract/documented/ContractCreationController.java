package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.common.utils.VariableHandlingUtil;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.CreateViewContract;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.contract.documented.ContractCreationService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

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

    private final VariableHandlingUtil variableHandlingUtil;

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
    public ResponseEntity<String> sendSignatureRequest(
            @RequestParam("userId") String userId,
            @RequestParam("signatureType") String signatureType,
            @RequestParam("signatureTypeLevel") String signatureTypeLevel) {

        String url = "https://demo.sign.netlock.hu/rest/signature/singleInitSignature";

        // 파일 경로를 지정
        File file = new File("C:/path/to/asd.pdf");
        FileSystemResource fileResource = new FileSystemResource(file);

        // 파라미터 구성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("mimeType", "application/pdf");
        params.add("userId", userId);
        params.add("fileName", "asd.pdf");
        params.add("signatureType", signatureType);
        params.add("signatureTypeLevel", signatureTypeLevel);

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
        ResponseEntity<String> response = restTemplate.exchange(
                url + "?mimeType=application/pdf&userId=" + userId +
                        "&fileName=asd.pdf&signatureType=" + signatureType +
                        "&signatureTypeLevel=" + signatureTypeLevel,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 응답 반환
        return response;
    }

}
