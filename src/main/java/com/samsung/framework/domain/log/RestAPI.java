package com.samsung.framework.domain.log;

import lombok.*;

import java.sql.Timestamp;

@ToString(callSuper = false)
@AllArgsConstructor
@Builder
@Getter
public class RestAPI {

    private long logSeq;
    private long userSeq;
    private long tokenSeq;
    private long menuSeq;
    private String logType;
    private String logEtc;

    //private String requestUri;
    //private String httpMethod;
    //private String resultJson;

    private int status;
    @Setter
    private String regId;
    private Timestamp regDttm;
    private String clientIp;
}
