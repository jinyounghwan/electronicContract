package com.samsung.framework.mapper.log;

import com.samsung.framework.domain.log.RestAPI;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestApiMapper {

    long insertApiAccessLog(RestAPI restApi);
    long updateApiAccessLog(RestAPI restApi);
}
