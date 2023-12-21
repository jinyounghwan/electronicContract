package com.samsung.framework.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.samsung.framework.common.enums.RequestTypeEnum;

/**
 * 데이터 변경이 일어나는 로직 실행 시, 애노테이션 추가 <br>
 * ex) LOGIN, CREATE, UPDATE, DELETE
 * @see RequestTypeEnum
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EntityLog {
}
