package com.samsung.framework.common.exception;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Getter
@Component
public class CustomFileException extends IOException {
    private int code;
    private String message;

    public CustomFileException(){
        this.code = ExceptionCodeMsgEnum.INVALID_FILE.getCode();
        this.message = ExceptionCodeMsgEnum.INVALID_FILE.getMsg();
    }

    public CustomFileException(int code, String message){
        this.code = code;
        this.message = message;
    }
}
