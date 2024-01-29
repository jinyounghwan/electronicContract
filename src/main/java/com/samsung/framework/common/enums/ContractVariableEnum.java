package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContractVariableEnum {

      NAME("{{name}}", "이름")
    , EMPLOYEE_NO("{{employee_no}}", "사번")
    , HIRE_DATE_HU("{{hire_date_hu}}", "입사일_hu")
    , HIRE_DATE_EN("{{hire_date_en}}", "입사일_en")
    , CONTRACT_DATE_HU("{{contract_date_hu}}", "계약일_hu")
    , CONTRACT_DATE_EN("{{contract_date_en}}", "계약일_en")
    , JOB_TITLE_HU("{{job_title_hu}}", "직무_hu")
    , JOB_TITLE_EN("{{job_title_en}}", "직무_en")
    , SALARY_NO("{{salary_no}}", "급여_숫자")
    , SALARY_HU("{{salary_hu}}", "급여_hu")
    , SALARY_EN("{{salary_en}}", "급여_en")
    , WAGE_TYPE_HU("{{wage_type_hu}}", "급여형태_hu")
    , WAGE_TYPE_EN("{{wage_type_en}}", "급여형태_en");

    private String key;
    private String desc;

}
