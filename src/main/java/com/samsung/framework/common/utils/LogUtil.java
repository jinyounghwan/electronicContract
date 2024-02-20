package com.samsung.framework.common.utils;

import com.samsung.framework.common.enums.MapKeyStringEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.log.LogMapper;
import com.samsung.framework.vo.log.LogSaveResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LogUtil {

    private final ValidationUtil validationUtil;
    private final LogMapper logMapper;

    public LogUtil(ValidationUtil validationUtil, LogMapper logMapper) {
        this.validationUtil = validationUtil;
        this.logMapper = logMapper;
    }

    /**
     *
     * @param logSaveRequest {@link LogSaveRequest}
     * @return resultMap {@link Map}
     */
    public Map<String, LogSaveResponse> saveLog(@Valid LogSaveRequest logSaveRequest) {
        log.info(logSaveRequest.toString());
        Map<String, LogSaveResponse> resultMap = new HashMap<>();

        if (validationUtil.parameterValidator(logSaveRequest, LogSaveRequest.class)) {
            int saved = logMapper.saveLog(logSaveRequest);
            if(saved > 0) {
                LogSaveResponse resp = LogSaveResponse.builder()
                        .resultCode(ResultCodeMsgEnum.REQUEST_SUCCESS.getCode())
                        .resultMsg(ResultCodeMsgEnum.REQUEST_SUCCESS.getMsg())
                        .build();
                resultMap.put(MapKeyStringEnum.SAVE_LOG.getKeyString(), resp);

                return resultMap;
            }
        }

        LogSaveResponse resp = LogSaveResponse.builder()
                .resultCode(ResultCodeMsgEnum.BAD_REQUEST.getCode())
                .resultMsg(ResultCodeMsgEnum.BAD_REQUEST.getMsg())
                .build();
        resultMap.put(MapKeyStringEnum.SAVE_LOG.getKeyString(), resp);

        return resultMap;
    }
    /**
    *   임직원 계약서 타입 확인
    * */
    
    public boolean getLogType (String contractNo , String type){
        return logMapper.getLogType(contractNo , type);
    }


}
