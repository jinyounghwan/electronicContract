package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum ContractProcessEnum {

//    계약서 생성/배부 상태
      CREATED("PRCS", "10", "01")
    , ASSIGNED("PRCS", "10", "02")
    , RECALLED("PRCS", "10", "03")
    , COMPLETED("PRCS", "10", "04")
    , PAPER_CONTRACT("PRCS", "10", "05")
//    계약 진행 상태
    , UNSEEN("PRCS", "20", "01")
    , VIEWED("PRCS", "20", "02")
    , SIGNED("PRCS", "20", "03")
    , REJECTED("PRCS", "20", "04")
//    계약 진행 히스토리(Log)
    , LOG_CREATE("PRCS", "30", "01")
    , LOG_ASSIGN("PRCS", "30", "02")
    , LOG_RECALL("PRCS", "30", "03")
    , LOG_SIGN_N_COMPLETE("PRCS", "30", "04")
    , LOG_REJECT("PRCS", "30", "05")
    , LOG_DOWNLOAD("PRCS", "30", "06")
    , LOG_PAPER_SIGN("PRCS", "30", "07")
    , LOG_VIEW("PRCS", "30", "08")
    ;

    private String prefix;
    private String parentCode;
    private String depth1;

    /**
     * 전체 메뉴 코드
     * @param key
     * @return
     */
    public static String processCode(ContractProcessEnum key) {
        return key.getPrefix() + key.getParentCode() + key.getDepth1();
    }

    /**
     * 계약서 상태 문자 반환
     * @param target
     * @return
     */
    public static Optional<ContractProcessEnum> getContractStatusName(String target) {
        return Arrays.stream(ContractProcessEnum.values()).filter(val -> {
            String enumProcessCode = val.getPrefix() + val.getParentCode() + val.getDepth1();
            return enumProcessCode.equals(target);
        }).findFirst();
    }
}
