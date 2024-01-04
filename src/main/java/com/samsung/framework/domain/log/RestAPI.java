package com.samsung.framework.domain.log;

import lombok.*;

import java.sql.Timestamp;

@ToString(callSuper = false)
@AllArgsConstructor
@Builder
@Getter
public class RestAPI {

    private long logSeq;
    private String logType;
    private String processStep;
    private String ipAddress;
    private String macAddress;
    private String createBy;
    private String createAt;
    //private String requestUri;
    //private String httpMethod;
    //private String resultJson;

    private int status;

}
