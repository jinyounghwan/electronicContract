package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.mapper.contract.documented.ContractPaperCompletionMapper;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractPaperCompService {
    private final ContractPaperCompletionMapper contractPaperCompletionMapper;
    public int getContractPaperCompTotal(AccountSearchVO searchVO, HttpServletRequest request){
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        searchVO.setEmpNo(account.getEmpNo());

        return contractPaperCompletionMapper.getContractPaperCompTotal(searchVO);
    }

    public List<ContractCompVO> getContractPaperList(AccountSearchVO accountSearchVO, HttpServletRequest request){
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        accountSearchVO.setEmpNo(account.getEmpNo());

        List<ContractCompVO> list = contractPaperCompletionMapper.getContractPaperCompList(accountSearchVO);
        list.forEach(data->{
            data.setCreatedAtStr(DateUtil.convertLocalDateTimeToString(data.getCreatedAt(),DateUtil.DATETIME_YMDHM_PATTERN));
            data.setSignDateAtStr(DateUtil.convertLocalDateTimeToString(data.getSignDate(), DateUtil.DATETIME_YMDHM_PATTERN));
        });
        return list;
    }

    public ContractCompVO getPaperCompInfo(long contractNo){
        ContractCompVO target = contractPaperCompletionMapper.getPaperCompInfo(contractNo);

        target.setCreatedAtStr(DateUtil.convertLocalDateTimeToString(target.getCreatedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        target.setSignDateAtStr(DateUtil.convertLocalDateTimeToString(target.getSignDate(), DateUtil.DATETIME_YMDHM_PATTERN));
        return target;
    }
}
