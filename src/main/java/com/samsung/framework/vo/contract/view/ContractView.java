package com.samsung.framework.vo.contract.view;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ContractView {
    private int contractNo;
    private String contractDateHu;
    private String contractDateEn;
    private String contractTitleEn;
    private String contractTitleHu;
    private String contentsEn;
    private String contentsHu;
    private String contractInfoEn;
    private String contractInfoHu;
    private String employeeInfoEn;
    private String employeeInfoHu;
    private String employerInfoEn;
    private String employerInfoHu;
}
