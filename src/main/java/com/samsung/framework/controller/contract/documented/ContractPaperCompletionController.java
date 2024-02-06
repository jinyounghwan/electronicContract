package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.account.AccountService;
import com.samsung.framework.service.contract.documented.ContractPaperCompService;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        mv.addObject("info", contractCompVO);
        return mv;
    }
}
