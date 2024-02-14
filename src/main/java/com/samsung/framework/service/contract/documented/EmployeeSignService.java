package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.contract.documented.ContractProgressMapper;
import com.samsung.framework.mapper.contract.documented.EmployeeSignMapper;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeSignService {
    private final EmployeeSignMapper signWaitMapper;
    private final LogUtil logUtil;
    private final ContractProgressMapper contractProgressMapper;

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
        boolean viewed = logUtil.getLogType(seq , LogTypeEnum.LOG_VIEW.toString());
        if(!viewed){
            LogSaveRequest saveRequest = LogSaveRequest.builder().logType(LogTypeEnum.LOG_VIEW)
                                                                 .processStep(LogTypeEnum.LOG_VIEW.getDescription())
                                                                 .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                                                                 .createdBy(account.getUserId())
                                                                 .contractNo(StringUtil.getString(seq))
                                                                 .build();
            Map<String, LogSaveResponse> logs = logUtil.saveLog(saveRequest);
            int result = contractProgressMapper.updateProgressStatus(seq);
        }
        return signWaitMapper.getContractWaitInfo(vo);
    }

    public ResultStatusVO updateProcessStatus(ContractVO contractVO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        contractVO.setUpdatedBy(account.getUserId());
        contractVO.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.COMPLETED));
        int result = signWaitMapper.updateProcessStatus(contractVO);
        if(result == 0){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(),ResultCodeMsgEnum.UPDATE_DATA_FAIL.name());
        }

        LogSaveRequest saveRequest = LogSaveRequest.builder().logType(LogTypeEnum.getLogTypeEnum(contractVO.getProcessStatus()))
                 .processStep(LogTypeEnum.getLogDescription(contractVO.getProcessStatus()))
                .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                .createdBy(account.getAdminId())
                .contractNo(StringUtil.getString(contractVO.getContractNo()))
                .build();
        Map<String, LogSaveResponse> logs = logUtil.saveLog(saveRequest);
        return new ResultStatusVO();
    }
}
