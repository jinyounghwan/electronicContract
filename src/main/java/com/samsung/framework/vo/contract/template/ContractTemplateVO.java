package com.samsung.framework.vo.contract.template;

import com.samsung.framework.common.enums.ContractTemplateEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ContractTemplateVO {

    private int templateSeq;
    private String templateTitle;
    private String templateType;
    @Setter
    private String templateTypeTitle;
    private String useYn;
    private String memo;
    private String contractTitleEn;
    private String contractTitleHu;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    @Builder
    public ContractTemplateVO(int templateSeq, String templateTitle, String templateType, String templateTypeTitle, String useYn, String memo, String contractTitleEn, String contractTitleHu, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
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
    }

}
