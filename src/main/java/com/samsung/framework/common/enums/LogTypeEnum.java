package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogTypeEnum {


    LOGIN("로그인")
    , CONTRACT_PROCESS("계약서 진행상황")
    , LOG_CREATE("PRCS3001")
    , LOG_ASSIGN("PRCS3002")
    , LOG_RECALL("PRCS3003")
    , LOG_SIGN_N_COMPLETE("PRCS3004")
    , LOG_REJECT("PRCS3005")
    , LOG_DOWNLOAD("PRCS3006")
    , LOG_PAPER_SIGN("PRCS3007")
    , LOG_VIEW("PRCS3008")
    ,PASSWORD_CHANGE("패스워드 변경");


    private String description;

    public static LogTypeEnum getLogTypeEnum (String target){
        if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.REJECTED))){
            return LogTypeEnum.LOG_REJECT;
        }else if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED))){
            return LogTypeEnum.LOG_ASSIGN;
        }
        return null;
    }
    public static String getLogDescription (String target){
        if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.REJECTED))){
            return LogTypeEnum.LOG_REJECT.getDescription();
        }else if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED))){
            return LogTypeEnum.LOG_ASSIGN.getDescription();
        }
        return null;
    }

}
