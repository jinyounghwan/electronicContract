package com.samsung.framework.vo.record;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode(of = {"logSeq", "tokenSeq", "userSeq", "menuSeq"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecordVO {
    private long logSeq;
    private long tokenSeq;
    private long userSeq;
    private long menuSeq;
    private String codeCd;
    private String codeNm;
    @Setter
    private String menuNm;
    private String clientIp;
    private String logType;
    @Setter
    private String logTypeKoreanStr;
    @Setter
    private String logEtc;
    private LocalDateTime regDt;
    @Setter
    private String regDtStr;
    private String regId;
}
