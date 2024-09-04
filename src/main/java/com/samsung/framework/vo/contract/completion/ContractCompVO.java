package com.samsung.framework.vo.contract.completion;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
@Builder
public class ContractCompVO {
    private long contractNo;
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
    private int salaryNo;
    private String salaryHu;
    private String salaryEn;
    private String wageTypeHu;
    private String wageTypeEn;
    private String signatureDataNo;
    private LocalDateTime signDate;
    private String validation;
    private String agreeYn;
    private String delYn;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private LocalDateTime assignedAt;
    private String templateType;
    private String templateTitle;
    private String contractTitleEn;
    private String createdAtStr;
    private String updatedAtStr;
    private String signDateAtStr;
    private String assignDateAtStr;
    private String userName;
    private String contractDate;
    private String firstName;
    private String lastName;
}
