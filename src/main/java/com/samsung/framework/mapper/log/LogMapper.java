package com.samsung.framework.mapper.log;

import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.vo.contract.view.HistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {

    int saveLog(LogSaveRequest logSaveRequest);
    boolean isExistsPasswordChangeLog(String createdBy);
    List<HistoryVO> getContractLogList(@Param(value="contractNo") int contractNo);
}
