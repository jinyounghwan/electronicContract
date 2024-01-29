package com.samsung.framework.service.account.ghr;

import com.samsung.framework.domain.account.ghr.GhrAccount;
import com.samsung.framework.mapper.account.ghr.GhrAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GhrAccountService {
    private final GhrAccountMapper ghrAccountMapper;
    public GhrAccount isExistsAccount(int empNo){
        return ghrAccountMapper.isExistsAccount(empNo);
    }
}
