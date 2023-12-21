package com.samsung.framework.common.interceptor;

import com.samsung.framework.common.enums.RequestTypeEnum;
import com.samsung.framework.domain.common.BaseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.springframework.util.StringUtils;

/**
 * 테이블 데이터 변경 이력 저장 인터셉터 (미사용)
 */
@Deprecated
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MyBatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        String method = invocation.getMethod().getName();
        log.info(args.toString());

        Object parameterObject = args[1];
        String requestObjName = parameterObject.getClass().getSimpleName();
        // 메뉴 접근 이력 제외 (주석 제거 할 것)
//        if(requestObjName.equals(RestAPI.builder().build().getClass().getSimpleName())) {
//            return invocation.proceed();
//        }

        MappedStatement mappedStatement = ((MappedStatement) args[0]);
        String mapperId = mappedStatement.getId();
        String sql = mappedStatement.getBoundSql(parameterObject).getSql();
        var paramList = mappedStatement.getBoundSql(parameterObject).getParameterMappings();

        /**
         * 1. INSERT, UPDATE, DELETE 쿼리 확인 (sqlCommandType)
         * 2. Parameter 확인 - param
         * 3. Object -> Json 변환
         */
        log.info("method: {}", method);
        log.info("parameterObject: {}", parameterObject);
        log.info("mapperId: {}", mapperId);
        log.info("sql: {}", sql);
        log.info("paramList: {}", paramList);

//        String resultMapId = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        log.info("sqlCommandType: {}", sqlCommandType);
        if(sqlCommandType.equals(SqlCommandType.INSERT)) {
            String tableName = sql.split(" ")[2];
            if(tableName.indexOf("(") > -1) {
                tableName = tableName.substring(0, tableName.indexOf("("));
            }
            log.info("tableName: {}", tableName);

            if(!(parameterObject instanceof BaseEntity)) {
                return invocation.proceed();
            }
            String insertSql = insert(tableName, RequestTypeEnum.CREATE, (BaseEntity) parameterObject);

            TextSqlNode sqlNode = new TextSqlNode(insertSql);
            DynamicSqlSource sqlSource = new DynamicSqlSource(mappedStatement.getConfiguration(), sqlNode);

            args[0] = copyFromMappedStatement(mappedStatement, sqlSource);
        }


        return invocation.proceed();
    }

    /**
     * 생성 쿼리 실행
     * @param ms
     * @param sqlSource
     * @return
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource sqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if(ms.getKeyProperties() != null && ms.getKeyProperties().length !=0){
            StringBuffer keyProperties = new StringBuffer();
            for(String keyProperty : ms.getKeyProperties()){
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length()-1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        //setStatementTimeout()
        builder.timeout(ms.getTimeout());

        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 저장 쿼리 실행
     * @param tableName
     * @param requestType
     * @param parameterObject
     * @return
     * @throws JsonProcessingException
     */
    private String insert(String tableName, RequestTypeEnum requestType, BaseEntity parameterObject) throws JsonProcessingException {
        StringBuffer insertSql = new StringBuffer("INSERT INTO ENTITY_LOG").append(" (");
        insertSql.append("ENTITY_NAME, LOG_ID1, LOG_TYPE, LOG_ID2, LOG_JSON, REMARK, REG_ID, REG_DTTM) VALUES(");
        // 여기부터 진행 - 파라미터 값 매핑 + 테이블 키값 조회 등등..
        StringBuffer propertySql = this.propertyValuesString(tableName, parameterObject, RequestTypeEnum.CREATE);

        insertSql.append(propertySql).append(")");
        return insertSql.toString();
    }

    /**
     * 컬럼 속성값 세팅
     * @param tableName
     * @param obj
     * @param logType
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    private <T extends BaseEntity> StringBuffer propertyValuesString(String tableName, T obj, RequestTypeEnum logType) throws JsonProcessingException {
        StringBuffer sb = new StringBuffer("");
        if(obj == null) {
            return sb;
        }

        sb.append("\"").append(tableName).append("\"").append(", ").append("\"").append(obj.getLogId1()).append("\"").append(", ").append("\"").append(logType.getRequestType()).append("\"");
        if(StringUtils.hasText(obj.getLogId2())) {
            sb.append(", ").append("\"").append(obj.getLogId2()).append("\"");
        }else {
            sb.append(", ").append("NULL");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String logJson = objectMapper.writeValueAsString(obj);
        logJson = "\"" + logJson.replace("\"", "\\\"") + "\"";

        sb.append(", ").append(logJson);

        if(StringUtils.hasText(obj.getRemark())) {
            sb.append(", ").append(obj.getRemark());
        }else {
            sb.append(", ").append("NULL");
        }

        sb.append(", ").append("\"").append(obj.getRegId()).append("\"");
        sb.append(", ").append("NOW()");

        return sb;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
