package com.samsung.framework.domain.log;

import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.utils.LogUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Builder
@Getter
public class LogSaveRequest {

    private long logSeq;

    @NotNull(message = "로그 유형은 필수 값 입니다.")
    private LogTypeEnum logType;

    private String processStep;

    @NotNull(message = "IP주소는 필수 값 입니다.")
    private String ipAddress;

    private String macAddress;

    @NotNull(message = "생성자는 필수 값 입니다.")
    private String createdBy;

    private String createdAt;
}
