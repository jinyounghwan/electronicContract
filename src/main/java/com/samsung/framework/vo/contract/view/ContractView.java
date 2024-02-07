package com.samsung.framework.vo.contract.view;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ContractView {
    private int contractNo;
    private int empNo;
    private String name;
    private String hireDateHu;
    private String hireDateEn;
    private String contractDateHu;
    private String jobTitleEn;
    private String jobTitleHu;
    private String salaryNo;
    private String salaryHu;
    private String salaryEn;
    private String wageTypeEn;
    private String wageTypeHu;
    private String contractTitleEn;
    private String contractTitleHu;
    private String contentsEn;
    private String contentsHu;
    private String contractDateEn;
    private String docStatus;
    private String processStatus;

    @Builder
    public ContractView(int contractNo,int empNo,String name, String hireDateEn, String hireDateHu
                        , String contractDateHu, String jobTitleEn, String jobTitleHu, String salaryEn,String salaryHu
                        , String salaryNo, String docStatus, String wageTypeEn, String wageTypeHu, String contractTitleEn, String contractTitleHu
                        , String contentsEn , String contentsHu, String contractDateEn, String processStatus){
        this.contractNo = contractNo;
        this.empNo = empNo;
        this.name = name;
        this.hireDateEn = hireDateEn;
        this.hireDateHu = hireDateHu;
        this.contractDateHu = contractDateHu;
        this.jobTitleEn = jobTitleEn;
        this.jobTitleHu =  jobTitleHu;
        this.salaryNo= salaryNo;
        this.salaryEn = salaryEn;
        this.salaryHu= salaryHu;
        this.wageTypeEn = wageTypeEn;
        this.wageTypeHu = wageTypeHu;
        this.contractTitleEn = contractTitleEn;
        this.contractTitleHu = contractTitleHu;
        this.contentsEn = contentsEn;
        this.contentsHu = contentsHu;
        this.contractDateEn = contractDateEn;
        this.docStatus = docStatus;
        this.processStatus = processStatus;
    }


}
