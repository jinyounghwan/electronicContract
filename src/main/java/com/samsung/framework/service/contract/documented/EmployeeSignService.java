package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.contract.documented.EmployeeSignMapper;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeSignService {
    private final EmployeeSignMapper signWaitMapper;

    public int getTotal(AccountSearchVO o , HttpServletRequest request) {
        return signWaitMapper.getTotal(o);
    }

    public List<ContractVO> getContractWaitsList(AccountSearchVO searchVO ,HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        searchVO.setEmpNo(account.getEmpNo());
        return signWaitMapper.getContractWaitsList(searchVO);
    }

    public ContractVO getContractWaitInfo(String seq , HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        ContractVO vo = ContractVO.builder().empNo(account.getEmpNo()).contractNo(StringUtil.getInt(seq)).build();
        return signWaitMapper.getContractWaitInfo(vo);
    }
}
