package com.samsung.framework.common.utils;

import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.vo.contract.ContractExcelVO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 객체 검증 유틸
 */
@Slf4j
@Component
public class ValidationUtil {
    private ValidatorFactory validatorFactory;
    private Validator validator;

    public ValidationUtil(ValidatorFactory validatorFactory, Validator validator) {
        this.validatorFactory = validatorFactory;
        this.validator = validator;
    }

    /**
     * 생성된 객체에 대한 검증
     * @param obj {@link Object}  검증 대상 객체
     * @param clazz {@link Class} 대상 객체 클래스
     * @return {@link Boolean}
     * @param <T>
     */
    public <T> boolean parameterValidator(T obj, Class<T> clazz) {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> validate = validator.validate((clazz.cast(obj)));

        if(validate.size() > 0) {
            log.error(validate.toString());
            for (ConstraintViolation<T> violation : validate) {
                log.error("[parameterValidator] {}", violation.getMessage());
            }
            return false;
        }

        return true;
    }

    /**
     * 요청 parameter 객체에 대한 검증 및 에러 데이터 반환
     * @param obj {@link Object}  검증 대상 객체
     * @param clazz {@link Class} 대상 객체 클래스
     * @return {@link Map}
     * @param <T>
     */
    public <T> Map<String, Object> clientRequestParameterValidator(T obj, Class<T> clazz) {
        var resultMap = new HashMap<String, Object>();
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> validate = validator.validate((clazz.cast(obj)));

        if(validate.size() > 0) {
            log.error(validate.toString());

            for (ConstraintViolation<T> violation : validate) {
                log.error("[parameterValidator] {}", violation.getMessage());
                resultMap.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            return resultMap;
        }

        return resultMap;
    }

    /**
     * 일괄업로드 엑셀 필수 데이터 검증
     * @param bulkList {@link List}
     * @param clazz {@link Class}
     * @return validated {@link ContractExcelVO}
     * @param <T>
     */
    public <T extends ContractExcelVO> T excelBulkDataValidator(List<T> bulkList, Class<T> clazz) {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        for (T obj : bulkList) {
            Set<ConstraintViolation<T>> validate = validator.validate((clazz.cast(obj)));

            if(validate.size() > 0) {
                log.error(validate.toString());
                for (ConstraintViolation<T> violation : validate) {
                    log.error("[excelBulkDataValidator] {}(empNo) / {}", obj.getEmpNo(), violation.getMessage());
                }
                return obj;
            }else {
                // TODO: IJ 유저정보 등록 여부 조회 및 검증 필요
            }
        }
        ContractExcelVO validated = new ContractExcelVO();
        validated.setEmpNo("00000000");

        return clazz.cast(validated);
    }
}
