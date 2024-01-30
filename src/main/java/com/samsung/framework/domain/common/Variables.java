package com.samsung.framework.domain.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class Variables {

    private String name;            // 이름
    private String employeeNo;      // 사번
    private String hireDateHu;      // 입사일_hu
    private String hireDateEn;      // 입사일_en
    private String contractDateHu;  // 계약일_hu
    private String contractDateEn;  // 계약일_en
    private String jobTitleHu;      // 직무_hu
    private String jobTitleEn;      // 직무_en
    private String salaryNo;        // 급여_숫자
    private String salaryHu;        // 급여_hu
    private String salaryEn;        // 급여_en
    private String wageTypeEn;      // 급여형태_en
    private String wageTypeHu;      // 급여형태_hu

}
