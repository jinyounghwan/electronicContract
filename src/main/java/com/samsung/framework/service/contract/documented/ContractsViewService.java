package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.utils.VariableHandlingUtil;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.mapper.contract.documented.ContractsViewMapper;
import com.samsung.framework.mapper.log.LogMapper;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.contract.view.HistoryVO;
import com.samsung.framework.vo.contract.view.ViewInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractsViewService {
    private final ContractsViewMapper contractsViewMapper;
    private final VariableHandlingUtil variableHandlingUtil;
    private final LogMapper logMapper;
    public ViewInfo getContractView(HttpServletRequest request, ProgressRequest param) {
        ContractView o = ContractView.builder().contractNo(param.getContractNo()).build();
        log.info("=============================> :{}" ,param.getContractNo() );
        // 계약서 정보
        ContractView view = contractsViewMapper.getContractView(o);
        // 변수에 맞게 치환 해줄 데이터 셋팅
//        Variables replacementTarget = Variables.builder()
//                                                .name(view.getName())
//                                                .employeeNo(String.valueOf(view.getEmpNo()))
//                                                .hireDateEn(view.getHireDateEn())
//                                                .hireDateHu(view.getHireDateHu())
//                                                .contractDateEn(view.getContractDateEn())
//                                                .contractDateHu(view.getContractDateHu())
//                                                .jobTitleEn(view.getJobTitleEn())
//                                                .jobTitleHu(view.getJobTitleHu())
//                                                .salaryNo(view.getSalaryNo())
//                                                .salaryEn(view.getSalaryEn())
//                                                .wageTypeEn(view.getWageTypeEn())
//                                                .wageTypeHu(replaceWageType(StringUtil.getString(view.getWageTypeEn())))
//                                                .build();
        Variables replacementTarget = Variables.builder()
                                        .name("Ikjoo")
                                        .employeeNo(String.valueOf(100003))
                                        .hireDateEn("01/03/2024")
                                        .hireDateHu("2024.03.01.")
                                        .jobTitleEn("Supervisor_EN")
                                        .jobTitleHu("Supervisor_HU")
                .contractDateEn("01/03/2024")
                .contractDateHu("2024.01.31")
                                        .salaryNo("458,900")
                                        .salaryEn("four hundred fifty eight thousand nine hundred")
                                        .wageTypeEn("month_EN")
                                        .wageTypeHu("hó")
                                        .build();
        String text = """
                the SAMSUNG SDI Magyarország Zrt. (registered seat: 2131 Göd Schenek István u. 1.;), as employer (hereinafter: „Employer”) and
                {{name}} (employee No.: {{employee_no}}), as employee (hereinafter „ Employee”)
                (hereinafter jointly referred to as „Contracting parties”) at the place and on the date below under the following conditions:
                                
                                
                1. Contracting parties modify by mutual consent the employment contract concluded on {{hire_date_en}} made by and between them (hereinafter: “Employment contract”) with effect from {{hire_date_en}} under the following:
                In addition:
                                
                a) The first sentence of Section 4. of the Employment contract is replaced by the following:
                The Employer and the Employee agree on that the job function of the Employee is: {{job_title_en}}.
                b) The first sentence of Section 6.1. of the Employment contract is replaced by the following:
                “6.1. The Employer and the Employee agree that the gross monthly base wage of the Employee is {{salary_no}} HUF/{{wage_type_en}}, i.e. [(확정 전)계약서 생성 시 입력_eng] {{salary_en}} Hungarian HUF/{{wage_type_hu}}.”
                                
                                
                2. Provisions of the Employment contract not affected by this amendment remain unchanged in force. To questions not regulated in this amendment of employment contract the provisions of the Labor Code shall apply.
                                
                3. This amendment of employment contract has been made in two (2) original samples in Hungarian and English, from which the Employee has received one sample. In the event of any inconsistency the Hungarian version shall apply.
                """;
        // en content
//        String replacedEn = variableHandlingUtil.replaceVariables(view.getContentsEn(), replacementTarget);
        String replacedEn = variableHandlingUtil.replaceVariables(text, replacementTarget);
        // hu content
//        String replacedHu = variableHandlingUtil.replaceVariables(view.getContentsHu() , replacementTarget);
        String replacedHu = variableHandlingUtil.replaceVariables(text , replacementTarget);
        return ViewInfo.builder()
                .name(view.getName())
                .contractTitleEn(view.getContractTitleEn())
                .contractTitleHu(view.getContractTitleHu())
                .contentsEn(view.getContentsEn())
                .contentsHu(view.getContentsHu())
                .contractDateHu(view.getContractDateHu())
                .jobTitleEn(view.getJobTitleEn())
                .jobTitleHu(view.getJobTitleHu())
                .docStatus(view.getDocStatus())
                .processStatus(view.getProcessStatus())
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

    public List<HistoryVO> getContractHistoryView(HttpServletRequest request, ProgressRequest param) {
        return  logMapper.getContractLogList(param.getContractNo());
    }
}
