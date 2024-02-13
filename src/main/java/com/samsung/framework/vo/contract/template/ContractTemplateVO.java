package com.samsung.framework.vo.contract.template;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ContractTemplateVO {
    private int templateSeq;
    private String templateTitle;
    private String templateTypeTitle;
    private String templateType;
    private String useYn;
    private String memo;
    private String contractTitleEn;
    private String contractTitleHu;
    private String contentsHu;
    private String contentsEn;
    private String employerInfoHu;
    private String employerInfoEn;
    private String employeeInfoHu;
    private String employeeInfoEn;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private int nextSeq;
    private String contractInfoEn;
    private String contractInfoHu;
    private int templateDetailsSeq;
}
