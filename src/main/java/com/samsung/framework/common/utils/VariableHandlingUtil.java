package com.samsung.framework.common.utils;

import com.samsung.framework.common.enums.ContractVariableEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.common.VariablesVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class VariableHandlingUtil {

    /**
     * 계약서 상 표기된 변수 각 임직원 데이터에 맞게 치환 후 반환
     * @param contents {@link String}
     * @param target {@link Variables}
     * @return contents {@link String}
     */
    public String replaceVariables(String contents, Variables target) {

        for (ContractVariableEnum var : ContractVariableEnum.values()) {
            switch(var) {
                case NAME -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getName());
                case EMPLOYEE_NO -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getEmployeeNo());
                case HIRE_DATE_HU -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getHireDateHu());
                case HIRE_DATE_EN -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getHireDateEn());
                case CONTRACT_DATE_HU -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getContractDateHu());
                case CONTRACT_DATE_EN -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getContractDateEn());
                case JOB_TITLE_HU -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getJobTitleHu());
                case JOB_TITLE_EN -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getJobTitleEn());
                case SALARY_NO -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getSalaryNo());
                case SALARY_HU -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getSalaryHu());
                case SALARY_EN -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getSalaryEn());
                case WAGE_TYPE_EN -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getWageTypeEn());
                case WAGE_TYPE_HU -> contents = contents.replaceAll(Pattern.quote(var.getKey()), target.getWageTypeHu());
            };
        }

//        log.info("REPLACED ###### \n {}", contents);

        return contents;
    }
}
