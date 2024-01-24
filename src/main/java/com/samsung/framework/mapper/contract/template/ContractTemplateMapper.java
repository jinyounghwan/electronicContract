package com.samsung.framework.mapper.contract.template;

import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractTemplateMapper {

    /**
     * 계약서 템플릿 코드 조회
     * @param target
     * @return
     */
    int getContractTemplateCode(int target);

    /**
     * 계약서 템플릿 목록 갯수 조회
     * @return
     */
    int getContractTemplateListCount(SearchVO searchVO);

    /**
     * 계약서 템플릿 목록 조회
     * @return
     */
    List<ContractTemplateVO> getContractTemplateList(SearchVO searchVO);

    ContractTemplateVO getContractTemplateInfo(String seq);

    int saveContractTemplateInfo(ContractTemplateVO contractTemplateVO);

    int saveContractTemplateCopyInfo(ContractTemplateVO contractTemplateVO);

    int saveContractTemplateCopyDetailsInfo(ContractTemplateVO contractTemplateVO);
}
