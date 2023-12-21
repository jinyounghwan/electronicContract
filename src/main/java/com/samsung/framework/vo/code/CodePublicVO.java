package com.samsung.framework.vo.code;

import lombok.*;

import java.util.Date;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CodePublicVO {
    // 코드 식별번호
    private int codeSeq;

    // 코드 값
    private String codeCd;

    // 코드명
    private String codeNm;

    // 코드 설명
    private String codeDesc;

    // 비고
    private String codeEtc;

    // 코드 순서
    private String codeOrd;

    // 사용유무
    private String userYn;

    // 삭제여부
    private String delYn;

    // 등록일
    private Date regDt;

    // 등록자
    private String regId;

    // 수정일
    private Date lastDt;

    // 수정자
    private String lastId;
}
