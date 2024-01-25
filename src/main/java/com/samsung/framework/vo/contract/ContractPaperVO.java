package com.samsung.framework.vo.contract;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ContractPaperVO {
    private long contractNo;
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
}
