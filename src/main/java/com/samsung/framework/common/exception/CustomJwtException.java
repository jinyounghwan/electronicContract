//package com.samsung.framework.common.exception;
//
//import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
//import lombok.Getter;
//import org.springframework.stereotype.Component;
//
///**
// * JWT 토큰 커스텀 Exception<br/>
// * 1. JWT 토큰과 관련된 예외처리 발생 시, 해당 exception 클래스 사용
// */
//@Getter
//@Component
//public class CustomJwtException extends RuntimeException {
//
//    private int code;
//    private String msg;
//
//    public CustomJwtException() {
//        this.code = ExceptionCodeMsgEnum.INVALID_TOKEN.getCode();
//        this.msg = ExceptionCodeMsgEnum.INVALID_TOKEN.getMsg();
//    }
//
//    /**
//     *
//     * @param exceptionCodeMsgEnum {@link ExceptionCodeMsgEnum}
//     */
//    public CustomJwtException(ExceptionCodeMsgEnum exceptionCodeMsgEnum) {
//        this.code = exceptionCodeMsgEnum.getCode();
//        this.msg = exceptionCodeMsgEnum.getMsg();
//    }
//}
