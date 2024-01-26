package com.samsung.framework.domain.contract.paper;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ContractComp {
    private long contractNo;
    @NotNull(message = "사번은 필수 값 입니다.")
    private int empNo;
    private int templateSeq;
    private String deptCode;
    private String processStep;
    private String contractBody;
    private String signatureDataNo;
    private String validation;
    private String agreeYn;
    private String delYn;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    @Builder
    public ContractComp(long contractNo, int empNo, int templateSeq, String deptCode, String processStep
            , String contractBody, String signatureDataNo, String validation, String agreeYn, String delYn, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt){
        this.contractNo = contractNo;
        this.empNo = empNo;
        this.templateSeq = templateSeq;
        this.deptCode = deptCode;
        this.processStep = processStep;
        this.contractBody = contractBody;
        this.signatureDataNo = signatureDataNo;
        this.validation = validation;
        this.agreeYn = agreeYn;
        this.delYn = delYn;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

}
