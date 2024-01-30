package com.samsung.framework.vo.contract.creation;

import lombok.*;

import javax.swing.text.StringContent;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractVO {
    private int contractNo;
    private int empNo;
    private int templateSeq;
    private String deptCode;
    private String docStatus;
    private String processStatus;
    private String name;
    private String hireDateHu;
    private String hireDateEn;
    private String contractDateHu;
    private String contractDateEn;
    private String jobTitleHu;
    private String jobTitleEn;
    private String salaryNo;
    private String salaryHu;
    private String salaryEn;
    private String wageTypeHu;
    private String wageTypeEn;
    private String signatureDataNo;
    private String signDate;
    private String validation;
    private String agreeYn;
    private String delYn;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
}
