package com.samsung.framework.vo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class SelectOptionVO {
    private long seq;
    private String code;
    private String displayName;
}
