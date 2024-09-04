package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.ContractTemplateEnum;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.account.AccountService;
import com.samsung.framework.service.contract.documented.ContractPaperCompService;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract/sign/paper/comp")
@Controller
public class ContractPaperCompletionController {
    private final ContractPaperCompService contractPaperCompService;
    private final AccountService accountService;
    @GetMapping({"/", ""})
    public String getCompletionPaperContractList(Model model, HttpServletRequest request){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("totalCount",contractPaperCompService.getContractPaperCompTotal(new AccountSearchVO(), request));
        return "contract/completion/contractPaperCompletionList";
    }

    @PostMapping("/list")
    public String getContractProgressList (Model model , @RequestBody AccountSearchVO searchVO, HttpServletRequest request){
        // total
        int totalCount = contractPaperCompService.getContractPaperCompTotal(searchVO , request);

        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(totalCount)
                .build();
        searchVO.setPaging(pagingVo);

        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractCompVO> list = contractPaperCompService.getContractPaperList(searchVO , request);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/completion/contractPaperCompletionList :: #content";
    }

    @GetMapping("/info/{contractNo}")
    public ModelAndView getPaperCompInfo(ModelAndView mv, @PathVariable long contractNo){
        ContractCompVO  contractCompVO = contractPaperCompService.getPaperCompInfo(contractNo);
        mv.setViewName("contract/completion/contractPaperCompletion-detail");
        contractCompVO.setDocStatus(ContractProcessEnum.getProcessStatus(contractCompVO.getDocStatus()));
        mv.addObject("info", contractCompVO);
        return mv;
    }

    @GetMapping("/view/{seq}")
    public ResponseEntity<String> viewPdf(@PathVariable String seq) throws IOException {

        ContractVO ContractVO = contractPaperCompService.getFileSeq(seq);

        //String filePath = "C:/files/electronicContract/upload/Contract/PDF/2405/2405101d0a065adbeb497ea2621fa49959b54e.pdf"; // Replace with your actual file path
        String filePath = ContractVO.getSignFilePath(); // file_integration > File Pat

        log.info("filePath select >" + filePath);

        byte[] pdfBytes = Files.readAllBytes(Paths.get(filePath));
        String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        log.info("base !! > " + base64EncodedPdf);

        return ResponseEntity.ok(base64EncodedPdf);
    }
}
