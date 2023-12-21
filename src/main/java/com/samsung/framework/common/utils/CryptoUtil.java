package com.samsung.framework.common.utils;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 암호화 유틸
 */
@Component
public class CryptoUtil {

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     * 비밀번호 암호화
     * @param rawPassword {@link String}
     * @return
     */
    public String encodePassword(String rawPassword) {
        if(!StringUtils.hasText(rawPassword)) {
            return new String();
        }

        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 로그인 시, 동일 비밀번호 여부 확인
     * @param rawPassword {@link String}
     * @return
     */
    public boolean isPasswordMatches(String rawPassword, String encodedPassword) {
        if(!StringUtils.hasText(rawPassword) || !StringUtils.hasText(encodedPassword)) {
            throw new IllegalArgumentException(ExceptionCodeMsgEnum.PASSWORD_VALUE_NOT_PROVIDED.getMsg());
        }
        
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
