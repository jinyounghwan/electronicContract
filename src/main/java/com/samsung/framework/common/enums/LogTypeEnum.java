package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogTypeEnum {


    LOGIN("로그인" , "LOGIN")
    , CONTRACT_PROCESS("계약서 진행상황" , "Contract Progress")
    , CREATE("PRCS3001","Create")
    , ASSIGN("PRCS3002" ,"Assign")
    , RECALL("PRCS3003" , "Recall")
    , SIGN("PRCS3004","Empl. Signed")
    , REJECT("PRCS3005" ,"Reject")
    , DOWNLOAD("PRCS3006","Download")
    , PAPER_SIGN("PRCS3007","Paper Sign")
    , VIEW("PRCS3008" , "View")
    , COMPLETED("PRCS3009", "Completed")
    , PASSWORD_CHANGE("패스워드 변경" , "Password Change");


    private String description;
    private String action;

    public static LogTypeEnum getLogTypeEnum (String target){
        if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.REJECTED))){
            return LogTypeEnum.REJECT;
        }else if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED))){
            return LogTypeEnum.SIGN;
        }
        return null;
    }
    public static String getLogDescription (String target){
        if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.REJECTED))){
            return LogTypeEnum.REJECT.getDescription();
        }else if(target.equals(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED))){
            return LogTypeEnum.SIGN.getDescription();
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
