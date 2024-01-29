package com.samsung.framework.service.contract.documented;

import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.mapper.contract.documented.ContractsViewMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractsViewService {
    private final ContractsViewMapper contractsViewMapper;

    public Object getContractView(HttpServletRequest request, ProgressRequest param) {
        return null;
    }
}
