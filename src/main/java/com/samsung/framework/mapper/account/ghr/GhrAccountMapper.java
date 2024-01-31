package com.samsung.framework.mapper.account.ghr;

import com.samsung.framework.domain.account.ghr.GhrAccount;
import com.samsung.framework.vo.account.ghr.GhtAccountVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GhrAccountMapper {
    GhrAccount isExistsAccount(int empNo);
    GhtAccountVO getGhrInfo(int empNo);
}
