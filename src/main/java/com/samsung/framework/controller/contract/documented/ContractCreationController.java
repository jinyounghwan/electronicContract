package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.service.contract.documented.ContractCreationService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.search.SearchVO;
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

    @ModelAttribute("templateCodeList")
    public List<Template> templatesOptionList() {
        return contractCreationService.getTemplateCode();
    }
    @GetMapping(value = {"/",""})
    public String contractCreation(){
        return "/contract/creation/contract-creation";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity saveContract(@Valid SaveContractRequest saveContractReq , HttpServletRequest request) {
        ResultStatusVO resultStatusVO = contractCreationService.saveContract(saveContractReq , request);
        return ResponseEntity.ok(resultStatusVO);
    }
}
