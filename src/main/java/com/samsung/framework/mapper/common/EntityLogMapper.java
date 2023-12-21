package com.samsung.framework.mapper.common;

import com.samsung.framework.domain.common.BaseEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EntityLogMapper extends GeneralMapper {

    String getTablePrimaryKey(String tableName);
    boolean isEntityLogExist(BaseEntity baseEntity);
    int insertEntityLogData(BaseEntity baseEntity);
    int updateEntityLogData(BaseEntity baseEntity);
    int insertEntityHistoryData(BaseEntity baseEntity);
}
