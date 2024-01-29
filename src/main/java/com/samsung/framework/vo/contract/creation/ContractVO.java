package com.samsung.framework.vo.contract.creation;

import lombok.*;

import javax.swing.text.StringContent;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ContractVO {
    private int contractNo;
    private int empNo;
    private int templateSeq;
    private String deptCode;
    private String docStatus;
    private String processStatus;
    private String contractBody;
    private String signatureDataNo;
    private String validation;
    private String agreeYn;
    private String delYn;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private String name;
    private String templateTitle;
    private String signDate;

    @Builder
    public ContractVO(int contractNo, int empNo, int templateSeq, String processStatus, String ContractBody, String signatureDataNo, String validation
                      ,String deptCode, String agreeYn, String delYn, String createdAt, String createdBy, String updatedAt, String updatedBy
                      , String name , String templateTitle , String docStatus , String signDate){
        this.contractNo = contractNo;
        this.empNo = empNo;
        this.templateSeq = templateSeq;
        this.processStatus = processStatus;
        this.contractBody = contractBody;
        this.signatureDataNo = signatureDataNo;
        this.validation = validation;
        this.agreeYn = agreeYn;
        this.delYn = delYn;
        this.deptCode = deptCode;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.templateTitle = templateTitle;
        this.name = name;
        this.docStatus = docStatus;
        this.signDate = signDate;


    }

}
