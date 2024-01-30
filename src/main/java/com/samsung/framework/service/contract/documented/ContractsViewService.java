package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.common.utils.VariableHandlingUtil;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.mapper.contract.documented.ContractsViewMapper;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.contract.view.ViewInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractsViewService {
    private final ContractsViewMapper contractsViewMapper;
    private final VariableHandlingUtil variableHandlingUtil;
    public ViewInfo getContractView(HttpServletRequest request, ProgressRequest param) {
        ContractView o = ContractView.builder().contractNo(param.getContractNo()).build();
        log.info("=============================> :{}" ,param.getContractNo() );
        // 계약서 정보
        ContractView view = contractsViewMapper.getContractView(o);
        // 변수에 맞게 치환 해줄 데이터 셋팅
        Variables replacementTarget = Variables.builder()
                                                .name(view.getName())
                                                .employeeNo(String.valueOf(view.getEmpNo()))
                                                .hireDateEn(view.getHireDateEn())
                                                .hireDateHu(view.getHireDateHu())
                                                .contractDateEn(view.getContractDateEn())
                                                .contractDateHu(view.getContractDateHu())
                                                .jobTitleEn(view.getJobTitleEn())
                                                .jobTitleHu(view.getJobTitleHu())
                                                .salaryNo(view.getSalaryNo())
                                                .salaryEn(view.getSalaryEn())
                                                .wageTypeEn(view.getWageTypeEn())
                                                .wageTypeHu(replaceWageType(StringUtil.getString(view.getWageTypeEn())))
                                                .build();
        // en content
        String replacedEn = variableHandlingUtil.replaceVariables(view.getContentsEn(), replacementTarget);
        // hu content
        String replacedHu = variableHandlingUtil.replaceVariables(view.getContentsHu() , replacementTarget);
        return ViewInfo.builder()
                .name(view.getName())
                .contractTitleEn(view.getContractTitleEn())
                .contractTitleHu(view.getContractTitleHu())
                .contentsEn(view.getContentsEn())
                .contentsHu(view.getContentsHu())
                .contractDateHu(view.getContractDateHu())
                .jobTitleEn(view.getJobTitleEn())
                .jobTitleHu(view.getJobTitleHu())
                .build();
    }
    private String replaceWageType(String type){
        String replaceType ="";
        switch (type) {
            case "month" -> {
                replaceType =  "hó";
            }
            case "hour" -> {
                replaceType =  "óra";
            }
            default -> {
                replaceType = "";
            }
        }
        return replaceType;
    }
}
