package com.samsung.framework.domain.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class Variables {

    private String name;
    private String employeeNo;
    private String hireDateHu;
    private String hireDateEn;
    private String contractDateHu;
    private String contractDateEn;
    private String jobTitleHu;
    private String jobTitleEn;
    private String salaryNo;
    private String salaryHu;
    private String salaryEn;
    private String wageTypeEn;
    private String wageTypeHu;

}
