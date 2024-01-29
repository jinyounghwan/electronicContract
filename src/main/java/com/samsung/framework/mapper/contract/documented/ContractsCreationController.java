package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.service.contract.documented.ContractsCreationService;
import com.samsung.framework.vo.common.ResultStatusVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contracts/create")
@Controller
public class ContractsCreationController {

    private ContractsCreationService contractsCreationService;

    @GetMapping(value={"/",""})
    public String contractBulkCreation(){
        return "/contract/creation/bulk-contract-creation";
    }

    @ResponseBody
    @PostMapping("/bulk-upload")
    public ResponseEntity bulkUpload(@RequestParam(value = "bulk") List<MultipartFile> multipartFiles) throws Exception {
        ResultStatusVO resultStatusVO = contractsCreationService.bulkUpload(multipartFiles);
        return ResponseEntity.ok(resultStatusVO);
    }
}
