package com.samsung.framework.service.contract.template;

import com.samsung.framework.common.enums.ContractTemplateEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.vo.common.CollectionPagingVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractTemplateService {

    private final ContractTemplateMapper contractTemplateMapper;

    /**
     * 계약서 템플릿 목록 조회
     * @return
     */
    public CollectionPagingVO getContractTemplateList(SearchVO searchVO) {
        List<ContractTemplateVO> contractTemplateList = new ArrayList<>();

        int templateCount = contractTemplateMapper.getContractTemplateListCount();
        if (templateCount > 0) {
            contractTemplateList = contractTemplateMapper.getContractTemplateList();
            contractTemplateList.forEach(item -> {
                item.setTemplateTypeTitle(ContractTemplateEnum.getTemplateTitle(item.getTemplateType()));
            });
        }

        return CollectionPagingVO.builder()
                .objects(contractTemplateList)
                .build();
    }

}
