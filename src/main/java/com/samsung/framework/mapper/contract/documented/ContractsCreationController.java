package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.service.contract.documented.ContractsCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contracts/create")
@Controller
public class ContractsCreationController {

    private final ContractsCreationService contractsCreationService;

    @GetMapping(value={"/",""})
    public String contractBulkCreation(){
        return "contract/creation/bulk-contract-creation";
    }

    @ResponseBody
    @PostMapping("/bulk-upload")
    public ResponseEntity bulkUpload(@RequestPart(value="file", required = true) List<MultipartFile> multipartFiles) throws Exception {
        Map<String, Object> resultMap= contractsCreationService.bulkUpload(multipartFiles);
        return ResponseEntity.ok().body(resultMap);
    }
}
