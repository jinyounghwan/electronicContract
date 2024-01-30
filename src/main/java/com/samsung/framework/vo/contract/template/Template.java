package com.samsung.framework.vo.contract.template;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Template {
    private String templateSeq;
    private String templateTitle;
}
