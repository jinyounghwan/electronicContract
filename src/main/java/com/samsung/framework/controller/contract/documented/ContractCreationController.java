package com.samsung.framework.controller.contract.documented;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
