package com.samsung.framework.domain.contract.paper;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ContractComp {
    private long contractNo;
    @NotNull(message = "사번은 필수 값 입니다.")
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
}
