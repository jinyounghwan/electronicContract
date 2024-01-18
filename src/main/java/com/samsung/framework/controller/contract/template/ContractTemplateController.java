package com.samsung.framework.controller.contract.template;

import com.samsung.framework.service.contract.template.ContractTemplateService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 계약서 템플릿 관련 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/template")
@Controller
public class ContractTemplateController {

    private final ContractTemplateService contractTemplateService;

    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity getContractTemplateList(@RequestBody SearchVO searchVO) {
        var result = contractTemplateService.getContractTemplateList(searchVO);

        return ResponseEntity.ok(result);
    }
}
