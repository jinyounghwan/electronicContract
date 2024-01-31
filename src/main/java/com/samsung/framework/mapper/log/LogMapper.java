package com.samsung.framework.mapper.log;

import com.samsung.framework.domain.log.LogSaveRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {

    int saveLog(LogSaveRequest logSaveRequest);
    boolean isExistsPasswordChangeLog(String createdBy);
}
