package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.vo.common.ResultStatusVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractCreationService {

    private final ContractCreationMapper contractCreationMapper;
}
