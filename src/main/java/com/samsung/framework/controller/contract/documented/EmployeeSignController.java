package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.service.contract.documented.EmployeeSignService;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("contract/sign")
@Controller
public class EmployeeSignController {
    private final EmployeeSignService signWaitService;

    @GetMapping(value="/wait")
    public String getContractProgress(Model model , HttpServletRequest request){
        model.addAttribute("paging" , new Paging());
        model.addAttribute("list" , new ArrayList<>());
        model.addAttribute("search" , new SearchVO());
        model.addAttribute("totalCount" ,signWaitService.getTotal(null , request));
        return "contract/signwait/list";
    }
    @PostMapping(value = "/wait/list")
    public String getContractProgressList (Model model , @RequestBody AccountSearchVO searchVO, HttpServletRequest request){
        Paging pagingVo =  Paging.builder()
                .currentPage(searchVO.getPaging().getCurrentPage())
                .displayRow(searchVO.getPaging().getDisplayRow())
                .totalCount(searchVO.getPaging().getTotalCount())
                .build();
        searchVO.setPaging(pagingVo);
        // total
        int totalCount = signWaitService.getTotal(searchVO , request);
        model.addAttribute("totalCount", totalCount);
        // paging
        model.addAttribute("paging",pagingVo);

        // list
        List<ContractVO> list = signWaitService.getContractWaitsList(searchVO , request);
        model.addAttribute("list",list);
        model.addAttribute("search" , searchVO);
        return "contract/signwait/list :: #content";
    }
    @GetMapping(value="/wait/info/{seq}")
    public String getContractWaitInfo(Model model ,@PathVariable String seq ,HttpServletRequest request){
        model.addAttribute("info",signWaitService.getContractWaitInfo(seq , request));
        return "contract/signwait/view";
    }
}
