package com.samsung.framework.vo.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Builder
@Getter
public class LogSaveResponse {
    private int resultCode;
    private String resultMsg;
}
