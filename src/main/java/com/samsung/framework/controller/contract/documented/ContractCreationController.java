package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.service.contract.documented.ContractCreationService;
import com.samsung.framework.vo.common.ResultStatusVO;
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

    @GetMapping(value = {"/",""})
    public String contractCreation(){
        return "/contract/creation/contract-creation";
    }
    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity saveContract(@Valid @RequestBody SaveContractRequest saveContractReq) {
        ResultStatusVO resultStatusVO = contractCreationService.saveContract(saveContractReq);
        return ResponseEntity.ok(resultStatusVO);
    }

}
