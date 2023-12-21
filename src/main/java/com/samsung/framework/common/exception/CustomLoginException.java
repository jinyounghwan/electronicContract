package com.samsung.framework.common.exception;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;

/**
 * 로그인 커스텀 Exception<br/>
 * 1. 로그인과 관련된 예외처리 발생 시, 해당 exception 클래스 사용
 */
@Getter
@Component
public class CustomLoginException extends GeneralSecurityException {

    private int code;
    private String msg;

    public CustomLoginException() {
        super();
    }

    public CustomLoginException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
