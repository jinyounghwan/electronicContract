package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.domain.contract.paper.ContractComp;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ContractCompletionMapper {
    int paperContractSave(ContractComp contractComp);
    int getTemplateSeq(ContractCompVO contractCompVO);
    int getContractCompTotal(SearchVO searchVO);
    List<ContractCompVO> getContractCompList(SearchVO searchVO);
    ContractCompVO getContractCompDetail(long contractCompVO);
    int getContractTemplateSeq(String templateType);
    FilePublicVO getFileSeq(String seq);
}
