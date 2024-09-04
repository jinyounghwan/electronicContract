package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import com.samsung.framework.vo.user.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractPaperCompletionMapper {
    List<ContractCompVO> getContractPaperCompList(AccountSearchVO searchVO);
    int getContractPaperCompTotal(AccountSearchVO searchVO);
    ContractCompVO getPaperCompInfo(long contractNo);
    ContractVO getFileSeq(String seq);
}
