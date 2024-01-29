package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.contract.documented.ContractProgressMapper;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractProgressService {
    private final ContractProgressMapper contractProgressMapper;
    private final LogUtil logUtil;
    public int getContractProgressTotal(SearchVO searchVO) {
        return contractProgressMapper.getContractProgressTotal(searchVO);
    }

    public List<ContractVO> getContractProgressList(SearchVO searchVO) {
        List<ContractVO> list = contractProgressMapper.getContractProgressList(searchVO);
        list.forEach(e -> {e.setDocStatus(ContractProcessEnum.getProcessStatus(e.getDocStatus()));
            e.setProcessStatus(ContractProcessEnum.getProcessStatus(e.getProcessStatus()));
        });
        return list;
    }

    public ResultStatusVO updateContractRecall(List<ProgressRequest> progressRequest ,HttpServletRequest request) {
        progressRequest.forEach(el -> el.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.RECALLED)));
        int result = contractProgressMapper.updateContractDocStatus(progressRequest);
        if(result == 0 ){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(), ResultCodeMsgEnum.UPDATE_DATA_FAIL.getMsg());
        }
        return new ResultStatusVO();
    }

    public ResultStatusVO updateContractAssign(List<ProgressRequest> list , HttpServletRequest request) {
        list.forEach(el -> el.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.ASSIGNED)));
        int result = contractProgressMapper.updateContractDocStatus(list);
        if(result == 0 ){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(), ResultCodeMsgEnum.UPDATE_DATA_FAIL.getMsg());
        }

        return new ResultStatusVO();
    }

    public ContractVO getContractProgressInfo(String seq) {
        ContractVO contractVO =  contractProgressMapper.getContractProgressInfo(seq);
        contractVO.setDocStatus(ContractProcessEnum.getProcessStatus(contractVO.getDocStatus()));
        contractVO.setProcessStatus(ContractProcessEnum.getProcessStatus(contractVO.getProcessStatus()));
        return contractVO;
    }

    public ResultStatusVO updateRecallInfo(ProgressRequest progressRequest ,HttpServletRequest request) {
        progressRequest.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.RECALLED));
        ContractVO contractVO  = ContractVO.builder().contractNo(progressRequest.getContractNo()).docStatus(progressRequest.getDocStatus()).build();
        int result = contractProgressMapper.updateContractDocStatusInfo(contractVO);
        if(result == 0 ){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(), ResultCodeMsgEnum.UPDATE_DATA_FAIL.getMsg());
        }
        this.saveLost(request,LogTypeEnum.LOG_RECALL);
        return new ResultStatusVO();
    }

    public ResultStatusVO updateAssignInfo(ProgressRequest progressRequest , HttpServletRequest request) {
        progressRequest.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.ASSIGNED));
        ContractVO contractVO  = ContractVO.builder().contractNo(progressRequest.getContractNo()).docStatus(progressRequest.getDocStatus()).build();
        int result = contractProgressMapper.updateContractDocStatusInfo(contractVO);
        if(result == 0 ){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(), ResultCodeMsgEnum.UPDATE_DATA_FAIL.getMsg());
        }
        return new ResultStatusVO();
    }

    private void saveLost(HttpServletRequest request , LogTypeEnum type){
        var logSaveRequest = LogSaveRequest.builder()
                .logType(type)
                .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                .createdBy("admin")
                .build();
        Map<String, LogSaveResponse> resultMap = logUtil.saveLog(logSaveRequest);
    }

}
