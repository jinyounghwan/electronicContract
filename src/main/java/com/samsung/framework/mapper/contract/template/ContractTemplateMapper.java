package com.samsung.framework.mapper.contract.template;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractTemplateMapper {

    /**
     * 계약서 템플릿 코드 조회
     * @param target
     * @return
     */
    int getContractTemplateCode(int target);
}
