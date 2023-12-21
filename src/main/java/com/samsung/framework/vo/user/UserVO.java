package com.samsung.framework.vo.user;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode(of = {"logSeq", "tokenSeq", "userSeq", "menuSeq"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserVO {
    private long userSeq;
    private String userType;
    private String userId;
    private String userNm;
    private String deptCd;
    private String deptNm;
    private String appointCd;
    private String appointNm;
    private String positionCd;
    private String positionNm;
    private String userTel;
    private String userEmail;
    private String userEtc;
    private String useYn;
    private String delYn;
    private LocalDateTime regDt;
    private String regId;
    private LocalDateTime lastDt;
    private String lastId;
}
