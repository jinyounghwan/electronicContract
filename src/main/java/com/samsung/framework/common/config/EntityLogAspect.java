/*
package com.samsung.framework.common.config;

import com.samsung.framework.common.utils.*;
import com.samsung.framework.domain.common.BaseEntity;
import com.samsung.framework.mapper.common.EntityLogMapper;
import com.samsung.framework.vo.member.MemberVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class EntityLogAspect extends CommonUtil {

    private final EntityLogMapper entityLogMapper;
    private final ObjectMapper objectMapper;
    private final ValidationUtil validationUtil;

    public EntityLogAspect(TokenFactory tokenFactory, SessionFactory sessionFactory, CookieFactory cookieFactory, EntityLogMapper entityLogMapper, ObjectMapper objectMapper, ValidationUtil validationUtil) {
        super(tokenFactory, sessionFactory, cookieFactory);
        this.entityLogMapper = entityLogMapper;
        this.objectMapper = objectMapper;
        this.validationUtil = validationUtil;
    }

    @AfterReturning(value = "@annotation(com.samsung.framework.common.annotation.EntityLog)", returning = "retVal")
    public void saveEntityLog(JoinPoint joinPoint, Object retVal) throws Throwable {
        Object[] args = joinPoint.getArgs();
//        String parameterName = args[0].getClass().getSimpleName();
//        String methodName = joinPoint.getSignature().getName();
//        String tablePrimaryKey = entityLogMapper.getTablePrimaryKey(((BaseEntity) args[0]).getTableName());

        */
/**
         * 1. ENTITY_LOG 저장 대상 객체 세팅
         * 2. ENTITY_LOG & ENTITY_HISTORY 저장
         * 3. 처리 유형(logType) 별 분기 처리
         * *//*

        if(retVal != null) {
            BaseEntity target = null;

            // LOGIN
            if(retVal.getClass().equals(MemberVO.class)) {
                MemberVO castObj = (MemberVO) retVal;
                target = entitySaveTargetSetter(castObj);
            }

            // CREATE, UPDATE, DELETE(RequestTypeEnum)
            if((retVal.getClass().equals(Integer.class) && ((Integer) retVal).intValue() > 0)) {
                BaseEntity castObj = (BaseEntity) args[0];
                target = entitySaveTargetSetter(castObj);
            }

            if(target != null) {
                storeEntityLog(target);
            }
        }
    }

    */
/**
     * ENTITY_LOG & ENTITY_HISTORY 저장 대상 객체 Setter
     * @param castObj
     * @return {@link BaseEntity}
     * @throws JsonProcessingException
     *//*

    private BaseEntity entitySaveTargetSetter(BaseEntity castObj) throws JsonProcessingException {
        BaseEntity target = new BaseEntity(castObj.getTableName()
                , castObj.getLogId1()
                , castObj.getLogId2()
                , castObj.getLogType()
                , objectMapper.writeValueAsString(castObj)
                , castObj.getRemark()
                , castObj.getRegId());

        validationUtil.parameterValidator(target, BaseEntity.class);

        return target;
    }

    */
/**
     * ENTITY_LOG & ENTITY_HISTORY 저장
     * @param target {@link BaseEntity}
     *//*

    private void storeEntityLog(BaseEntity target) {
        boolean isEntityLogExist = entityLogMapper.isEntityLogExist(target);

        switch(target.getLogType()) {
            // CREATE
            case "02" -> {
                if(!isEntityLogExist) {
                    int insert = entityLogMapper.insertEntityLogData(target);
                    log.info("ENTITY_LOG INSERT: {}", insert);
                    if(insert > 0) {
                        entityLogMapper.insertEntityHistoryData(target);
                    }
                }
                log.info("ENTITY_LOG TARGET: {}", target);
            }
            // LOGIN, UPDATE, DELETE
            case "00", "03", "04" -> {
                if(isEntityLogExist) {
                    int update = entityLogMapper.updateEntityLogData(target);
                    log.info("ENTITY_LOG UPDATE: {}", update);
                    if(update > 0) {
                        entityLogMapper.insertEntityHistoryData(target);
                    }
                }else {
                    int insert = entityLogMapper.insertEntityLogData(target);
                    log.info("ENTITY_LOG INSERT: {}", insert);
                    if(insert > 0) {
                        entityLogMapper.insertEntityHistoryData(target);
                    }
                }
                log.info("ENTITY_LOG TARGET: {}", target);
            }
            default -> log.info("ENTITY_LOG DEFAULT: {}", target);
        }
    }
}
*/
