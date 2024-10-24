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
import org.springframework.core.io.InputStreamResource;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    // Api 세션 토큰
    String signatureSessionToken ="";


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


}
