package com.samsung.framework.vo.test;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = {"code", "name"})
@AllArgsConstructor
@Getter @Setter
public class TestVO {

    private int code;
    private String name;
}
