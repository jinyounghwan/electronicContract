package com.samsung.framework.domain.record;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Record {
    //로그 SEQ
    private long logSeq;
    //토큰 SEQ
    private long tokenSeq;
    //회원 SEQ
    private long userSeq;
    //메뉴 SEQ
    private long menuSeq;
    //처리 유형
    private String logType;
    //비고
    private String logEtc;
    //등록일
    private LocalDateTime regDt;
    private String regDtStr;
    //등록자
    private String regId;

    @Builder
    public Record(long logSeq, long tokenSeq, long userSeq, long menuSeq, String logType, String logEtc, LocalDateTime regDt, String regDtStr, String regId) {
        this.logSeq = logSeq;
        this.tokenSeq = tokenSeq;
        this.userSeq = userSeq;
        this.menuSeq = menuSeq;
        this.logType = logType;
        this.logEtc = logEtc;
        this.regDt = regDt;
        this.regDtStr = regDtStr;
        this.regId = regId;
    }
}
