package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogTypeEnum {


    LOGIN("로그인" , "LOGIN")
    , CONTRACT_PROCESS("계약서 진행상황" , "Contract Progress")
    , LOG_CREATE("PRCS3001","Create")
    , LOG_ASSIGN("PRCS3002" ,"Assign")
    , LOG_RECALL("PRCS3003" , "Recall")
    , LOG_SIGN_N_COMPLETE("PRCS3004","Sign & Complete")
    , LOG_REJECT("PRCS3005" ,"Reject")
    , LOG_DOWNLOAD("PRCS3006","Download")
    , LOG_PAPER_SIGN("PRCS3007","Paper Sign")
    , LOG_VIEW("PRCS3008" , "View")
    ,PASSWORD_CHANGE("패스워드 변경" , "Password Change");


    private String description;
    private String action;

    public static LogTypeEnum getLogTypeEnum (String target){
        if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.REJECTED))){
            return LogTypeEnum.LOG_REJECT;
        }else if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED))){
            return LogTypeEnum.LOG_SIGN_N_COMPLETE;
        }
        return null;
    }
    public static String getLogDescription (String target){
        if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.REJECTED))){
            return LogTypeEnum.LOG_REJECT.getDescription();
        }else if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED))){
            return LogTypeEnum.LOG_SIGN_N_COMPLETE.getDescription();
        }
        return null;
    }

    public static String getAction(String processCode ){
        for (LogTypeEnum item : LogTypeEnum.values()){
            if(processCode.equals(item.getDescription())){
                return item.action.toString();
            }
        }
        return new String();
    }

}
