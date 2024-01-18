package com.samsung.framework.mapper.token;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenMapper {

    int isTokenExist(String memberId);
    boolean isValidToken(@Param("token") String token, @Param("reissue") String reissue);
    String findTokenSeq(String memberId);
}
