package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.service.contract.template.ContractTemplateService;
import com.samsung.framework.vo.common.ResultStatusVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 계약서 생성 관련 컨트롤러
 */
@Slf4j
@RequestMapping("/contract/creation")
@Controller
public class ContractCreationController {

    private final ContractTemplateService contractTemplateService;

    public ContractCreationController(ContractTemplateService contractTemplateService) {
        this.contractTemplateService = contractTemplateService;
    }

    @PostMapping("/saveContract")
    public ResponseEntity saveContract(@Valid @RequestBody SaveContractRequest saveContractReq) {

        ResultStatusVO resultStatusVO = contractTemplateService.saveContract(saveContractReq);

        return ResponseEntity.ok(resultStatusVO);
    }
}
