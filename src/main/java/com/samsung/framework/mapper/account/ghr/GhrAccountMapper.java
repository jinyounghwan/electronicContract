package com.samsung.framework.mapper.account.ghr;

import com.samsung.framework.domain.account.ghr.GhrAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GhrAccountMapper {
    GhrAccount isExistsAccount(int empNo);
}
