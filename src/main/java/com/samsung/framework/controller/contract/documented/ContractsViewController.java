package com.samsung.framework.controller.contract.documented;

import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.service.contract.documented.ContractsViewService;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.contract.view.ViewInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/contract/view")
@RequiredArgsConstructor
@Slf4j
public class ContractsViewController {
    private final ContractsViewService contractsViewService;

    @PostMapping(value = {"",""})
    @ResponseBody
    public ResponseEntity getContractView(HttpServletRequest request , @RequestBody ProgressRequest param){
        ViewInfo o = contractsViewService.getContractView(request , param);
        return new ResponseEntity(o, HttpStatus.OK);
    }

}
