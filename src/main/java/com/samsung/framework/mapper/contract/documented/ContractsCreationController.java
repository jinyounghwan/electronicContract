package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.common.enums.FileTypeEnum;
import com.samsung.framework.service.contract.documented.ContractsCreationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String contractBulkCreation(Model model){
        model.addAttribute("fileSeq", FileTypeEnum.EXCEL.getTemplateDownloadFileSeq());
        return "contract/creation/bulk-contract-creation";
    }

    @ResponseBody
    @PostMapping("/bulk-upload")
    public ResponseEntity bulkUpload(@RequestPart(value="file", required = true) List<MultipartFile> multipartFiles , HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap= contractsCreationService.bulkUpload(multipartFiles , request);
        return ResponseEntity.ok().body(resultMap);
    }
}
