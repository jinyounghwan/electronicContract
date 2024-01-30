package com.samsung.framework.vo.contract.view;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ViewInfo {
    private String contractTitleHu;
    private String contractTitleEn;
    private String contentsEn;
    private String contentsHu;
    private String contractDateHu;
    private String name;
    private String jobTitleEn;
    private String jobTitleHu;
}
