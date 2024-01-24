package com.samsung.framework.vo.contract.template;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ContractTemplateVO {

    private int templateSeq;
    private String templateTitle;
    private String templateTypeTitle;
    private String templateType;
    private String useYn;
    private String memo;
    private String contractTitleEn;
    private String contractTitleHu;
    private String preambleHu;
    private String preambleEn;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private int nextSeq;

    @Builder
    public ContractTemplateVO(int templateSeq, String templateTitle, String templateType, String templateTypeTitle, String useYn, String memo, String contractTitleEn, String contractTitleHu, String createdBy,
                                String createdAt, String updatedBy, String updatedAt , String preambleHu, String preambleEn) {
        this.templateSeq = templateSeq;
        this.templateTitle = templateTitle;
        this.templateType = templateType;
        this.templateTypeTitle = templateTypeTitle;
        this.useYn = useYn;
        this.memo = memo;
        this.contractTitleEn = contractTitleEn;
        this.contractTitleHu = contractTitleHu;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        this.preambleHu =  preambleHu;
        this.preambleEn = preambleEn;
    }
}
