package com.samsung.framework.vo.common;

import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.FieldError;

import java.util.List;

@ToString
@AllArgsConstructor
@Getter @Setter
public class ResultStatusVO {

    private int code;
    private String message;
    private String specificMsg;
    private List<FieldError> fieldErrors;

    public ResultStatusVO() {
        this.code = ResultCodeMsgEnum.REQUEST_SUCCESS.getCode();
        this.message = ResultCodeMsgEnum.REQUEST_SUCCESS.getMsg();
    }

    public ResultStatusVO(String message) {
        this.message = message;
    }

    public ResultStatusVO(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
