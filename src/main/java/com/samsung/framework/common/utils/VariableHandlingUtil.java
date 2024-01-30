package com.samsung.framework.common.utils;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.domain.common.Variables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class VariableHandlingUtil {

    private final ValidationUtil validationUtil;

    public VariableHandlingUtil(ValidationUtil validationUtil) {
        this.validationUtil = validationUtil;
    }

    /**
     * 계약서 상 표기된 변수 각 임직원 데이터에 맞게 치환 후 반환
     * @param contents {@link String}
     * @param target {@link Variables}
     * @return contents {@link String}
     */
    public String replaceVariables(String contents, Variables target) {
        if (!validationUtil.parameterValidator(target, Variables.class)) {
            throw new IllegalArgumentException(ExceptionCodeMsgEnum.INVALID_ARGUMENT_EXISTS.toString());
        }
        Field[] declaredFields = target.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value;

            try {
                value = field.get(target);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }

            if(!ObjectUtils.isEmpty(value)) {
                contents = contents.replaceAll(Pattern.quote(attachVariableIdentifier(convertToSnakeCase(field.getName()))), value.toString());
            }
        }

//        log.info("REPLACED ###### \n {}", contents);

        return contents;
    }

    /**
     * String to Variable Converter
     * @param target
     * @return
     */
    private String attachVariableIdentifier(String target) {
        return "{{" + target + "}}";
    }

    /**
     * Camel Case -> Snake Case Converter
     * @param target {@link String}
     * @return {@link String}
     */
    private String convertToSnakeCase(String target) {
        Pattern pattern = Pattern.compile("([a-z])([A-Z])");
        Matcher matcher = pattern.matcher(target);
        return matcher.replaceAll(matchResult -> String.format("%s_%s", matchResult.group(1), matchResult.group(2).toLowerCase()));
    }
}
